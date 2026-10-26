package productionplan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class NpplanDDT {

	// =====================================================================
	// SETTINGS
	// =====================================================================
	static final String EXCEL_PATH = "C:\\Users\\Satyender STPL\\OneDrive - Saisatwik Technologies Private Limited\\Documents\\MyWorks\\Production_plan_Entry.xlsx";
	static final String SHEET_NAME = "Sheet1";
	static final String URL = "https://mavro.app/";
	static final String USERNAME = "Test@mavro";
	static final String PASSWORD = "1234567890";

	static final boolean SAVE_PLAN_AT_END = true;

	static final String DEF_APPROX = "1";
	static final String DEF_APPROX_UNIT = "H";
	static final String DEF_LABOUR_TYPE = "MAN";
	static final String DEF_LABOUR_DUR = "1";
	static final String DEF_LABOUR_UNIT = "H";

	// =====================================================================
	// Data holders
	// =====================================================================
	static class Plan {
		String name = "", itemCode = "", qty = "", unit = "", office = "", process = "";
	}

	static class Line {
		int excelRow;
		String sfg = "", qty = "", unit = "";
		List<String> items = new ArrayList<>();
		String approx = "", labType = "", labDur = "", labUnit = "";
	}

	static class SubGroup {
		String name, key;
		List<Line> lines = new ArrayList<>();
		SubGroup(String name) { this.name = name; this.key = squash(name); }
	}

	static WebDriver driver;
	static JavascriptExecutor js;
	static String stage = "";

	static Plan plan = new Plan();
	static List<SubGroup> subs = new ArrayList<>();

	// =====================================================================
	// Locators
	// =====================================================================
	static final String CTRL = "div[@class='searchable-select-control']";
	static final String BLOCKS = "//div[@class='mt-3 production-plan-subprocess-section row']/div[@class='col-md-12']/div";

	static String blk(int j) { return "(" + BLOCKS + ")[" + j + "]"; }
	static String sfg(int j, int s) { return blk(j) + "/div[2]/div[" + s + "]"; }

	// =====================================================================
	// MAIN
	// =====================================================================
	public static void main(String[] args) throws Exception {

		readExcel();
		printExcelSummary();

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ZERO);
		js = (JavascriptExecutor) driver;
		driver.get(URL);

		try {
			login();
			openProductionPlan();
			fillPlanHeader();
			selectProcess();
			fillAllSubProcesses();
			savePlan();
			System.out.println("\nDONE: All Production Plan data filled and saved successfully!");
		} catch (Exception e) {
			System.out.println("\nSTOPPED at stage: " + stage);
			System.out.println("Reason: " + firstLine(e.getMessage()));
			screenshot("production-plan-error.png");
		}
	}

	static void login() throws Exception {
		stage("Login");
		findOne(By.xpath("//input[@type=\"text\"]")).sendKeys(USERNAME);
		findOne(By.xpath("//input[@type=\"password\"]")).sendKeys(PASSWORD);
		findOne(By.xpath("//button[@type=\"submit\"]")).click();
		Thread.sleep(5000);
	}

	static void openProductionPlan() throws Exception {
		stage("Menu Navigation: Production > Production Plan");
		findOne(By.xpath("//input[@placeholder=\"Search menu...\"]")).sendKeys("Production");
		Thread.sleep(1000);
		clickSafe(findOne(By.xpath("//*[span='Production']")));
		clickSafe(findOne(By.xpath("//*[span=\"Production Plan\"]")));
	}

	static void fillPlanHeader() throws Exception {
		stage("Plan Name");
		clickSafe(findOne(By.xpath("//div[@class=\"production-plan-container container-fluid\"]")));
		findOne(By.xpath("//input[@placeholder=\"Plan name\"]")).sendKeys(plan.name);
		Thread.sleep(10000);

		stage("Item Code");
		chooseRequired(By.xpath("//span[normalize-space()='Search and select item...']"), plan.itemCode, "Item Code");

		stage("Production Quantity");
		setInput(By.xpath("//input[@name='productionQuantity']"), plan.qty);

		stage("Unit");
		try {
			String u = chooseFromDropdown(By.xpath("//div[@class='production-plan-header-card']//div[4]//div[1]//div[1]//div[1]"), plan.unit);
			if (u == null) System.out.println("   WARN: Unit '" + plan.unit + "' not found in dropdown, skipped.");
		} catch (IllegalStateException e) {
			System.out.println("   WARN: Unit dropdown did not open, skipped.");
		}

		stage("Office Code");
		chooseRequired(By.xpath("//div[@class='production-plan-header-card']//div[5]//div[1]//div[1]//div[1]"), plan.office, "Office Code");
	}

	static void selectProcess() throws Exception {
		stage("Production Process: " + plan.process);
		chooseRequired(By.xpath("//div[@class='col-md-11']//div[@class='searchable-select-control']"), plan.process, "Production Process");
		waitCountAtLeast(By.xpath(BLOCKS), 1, 15, "Sub-process blocks did not render after selecting Process");
		Thread.sleep(1500);
	}

	static void fillAllSubProcesses() throws Exception {
		int blocks = count(By.xpath(BLOCKS));

		List<String> heads = new ArrayList<>();
		for (int j = 1; j <= blocks; j++) {
			String t = driver.findElement(By.xpath(blk(j))).getAttribute("textContent");
			heads.add(squash(t));
		}

		for (int si = 0; si < subs.size(); si++) {
			SubGroup sg = subs.get(si);

			int j = -1;
			for (int k = 0; k < heads.size(); k++) {
				if (heads.get(k).startsWith(sg.key)) { j = k + 1; break; }
			}
			if (j < 0) {
				j = si + 1;
				System.out.println("WARN: Sub-process '" + sg.name + "' not matched on UI. Fallback to index position " + j);
			}
			if (j > blocks) throw new IllegalStateException("Sub-process '" + sg.name + "' is out of range on UI");

			System.out.println("Sub-process " + (si + 1) + "/" + subs.size() + ": " + sg.name + " (" + sg.lines.size() + " SFG item(s))");
			fillSubProcess(j, sg);
		}
	}

	static void fillSubProcess(int j, SubGroup sg) throws Exception {
		int n = sg.lines.size();
		for (int s = 1; s <= n; s++) {
			Line ln = sg.lines.get(s - 1);
			if (s > 1) addSfgBlock(j, s, sg.name);
			fillSfgBlock(j, s, ln, sg.name);
			System.out.println("   " + sg.name + " - SFG " + s + "/" + n + " completed (Excel Row " + ln.excelRow + ")");
		}
		fillSubProcessFields(j, sg);
	}

	static void addSfgBlock(int j, int s, String subName) throws Exception {
		stage(subName + ": + Add SFG Item (SFG " + s + ")");
		By add = firstExisting(5,
				By.xpath(blk(j) + "/div[2]/button[1]"),
				By.xpath(blk(j) + "/div[2]/button[contains(normalize-space(.),'Add SFG Item')]"));
		clickSafe(findOne(add));
		waitCountAtLeast(By.xpath(sfg(j, s) + "/div[2]"), 1, 10, "SFG " + s + " block failed to appear after clicking '+ Add SFG Item'");
		Thread.sleep(800);
	}

	static void fillSfgBlock(int j, int s, Line ln, String subName) throws Exception {
		stage(subName + ", SFG " + s + " (Excel Row " + ln.excelRow + ")");
		String S = sfg(j, s);

		chooseRequired(By.xpath(S + "/div[2]/div[1]//" + CTRL), ln.sfg, "SFG Output Item");

		if (ln.qty.isEmpty()) throw new IllegalStateException("Output Qty is blank in Excel");
		setInput(By.xpath("(" + S + "/div[2]/div[2]//input)[1]"), ln.qty);

		if (ln.unit == null || ln.unit.isEmpty()) {
			ln.unit = "KG";
		}
		chooseRequired(By.xpath(S + "/div[2]/div[3]//" + CTRL), ln.unit, "Output Unit");

		// DYNAMIC ITEMS HANDLING: Single item or Multiple Items across all sub-processes
		for (int i = 1; i <= ln.items.size(); i++) {
			String item = ln.items.get(i - 1);
			
			// Strictly scoped inside div[3] (ITEMS FOR THIS SFG container)
			By itemCtrl = By.xpath("(" + S + "/div[3]//div[@class='searchable-select-control'])[" + i + "]");

			// If multiple items exist (i > 1), click "+ Add Item" to create the new box dynamically
			if (i > 1) {
				stage(subName + ", SFG " + s + ": Clicking + Add Item (Item " + i + ")");
				By addItemBtn = firstExisting(5,
						By.xpath(S + "/div[3]//button[contains(normalize-space(.),'Add Item')]"),
						By.xpath(S + "/div[3]//button[1]"));
				
				clickSafe(findOne(addItemBtn));
				Thread.sleep(800);
			}

			String chosen = chooseFromDropdown(itemCtrl, item);
			if (chosen == null) {
				if (i == 1) throw new IllegalStateException("Item '" + item + "' not found in dropdown (Excel Row " + ln.excelRow + ")");
				System.out.println("   WARN: Excel Row " + ln.excelRow + " - Item '" + item + "' not found in dropdown, skipped.");
			} else {
				System.out.println("      -> Item " + i + " selected: " + chosen);
			}
		}
	}

	static void fillSubProcessFields(int j, SubGroup sg) throws Exception {
		stage(sg.name + ": Approx Duration / Labour Details");

		String approx = firstNonEmpty(sg, l -> l.approx);
		String labType = firstNonEmpty(sg, l -> l.labType);
		String labDur = firstNonEmpty(sg, l -> l.labDur);
		String labUnit = firstNonEmpty(sg, l -> l.labUnit);

		boolean usedDefault = false;
		if (approx.isEmpty()) { approx = DEF_APPROX; usedDefault = true; }
		if (labType.isEmpty()) { labType = DEF_LABOUR_TYPE; usedDefault = true; }
		if (labDur.isEmpty()) { labDur = DEF_LABOUR_DUR; usedDefault = true; }
		if (labUnit.isEmpty()) { labUnit = DEF_LABOUR_UNIT; usedDefault = true; }
		labType = mapLabourType(labType);

		String B = blk(j);
		setInput(By.xpath("(" + B + "/div[3]//input)[1]"), approx);
		chooseRequired(By.xpath("(" + B + "/div[3]//" + CTRL + ")[1]"), DEF_APPROX_UNIT, "Approx Duration Unit");
		chooseRequired(By.xpath("(" + B + "/div[4]//" + CTRL + ")[1]"), labType, "Labour Type");
		setInput(By.xpath("(" + B + "/div[4]//input)[1]"), labDur);
		chooseRequired(By.xpath("(" + B + "/div[4]//" + CTRL + ")[2]"), labUnit, "Labour Duration Unit");

		System.out.println("   " + sg.name + " - Approx Duration / Labour completed" + (usedDefault ? " (Default values applied for blank Excel fields)" : ""));
	}

	static String firstNonEmpty(SubGroup sg, Function<Line, String> f) {
		for (Line l : sg.lines) {
			String v = f.apply(l);
			if (v != null && !v.isEmpty()) return v;
		}
		return "";
	}

	static String mapLabourType(String s) {
		String k = squash(s);
		if (k.equals("manpower")) return "MAN";
		if (k.equals("labour") || k.equals("labor")) return "LAB";
		if (k.equals("supervisor")) return "SUP";
		return s;
	}

	static void savePlan() throws Exception {
		if (!SAVE_PLAN_AT_END) {
			System.out.println("Plan save skipped (SAVE_PLAN_AT_END = false)");
			return;
		}
		stage("Save Production Plan");
		Thread.sleep(2000);
		clickSafe(findOne(By.xpath("//button[normalize-space()='Save Production Plan']")));
		Thread.sleep(5000);
	}

	static String chooseRequired(By controlBy, String text, String what) throws Exception {
		if (text == null || text.isEmpty()) throw new IllegalStateException(what + ": Input value is blank in Excel");
		String chosen = chooseFromDropdown(controlBy, text);
		if (chosen == null) throw new IllegalStateException(what + ": '" + text + "' not found in dropdown options");
		return chosen;
	}

	static String chooseFromDropdown(By controlBy, String excelText) throws Exception {
		String wanted = clean(excelText);
		WebElement control = findOne(controlBy);
		scrollCenter(control);
		Thread.sleep(400);

		WebElement search = null;
		for (int attempt = 1; attempt <= 3 && search == null; attempt++) {
			clickSafe(control);
			search = waitSearchBox(control, 3);
		}
		if (search == null) throw new IllegalStateException("Dropdown failed to expand: " + controlBy);

		String typed = wanted;
		List<WebElement> options = typeAndGetOptions(control, typed);
		while (options.isEmpty() && typed.length() > 2) {
			typed = typed.substring(0, typed.length() - 1);
			options = typeAndGetOptions(control, typed);
		}
		if (options.isEmpty()) {
			closeDropdown(control);
			return null;
		}

		WebElement pick = null;
		for (WebElement o : options) {
			if (isExact(optionText(o), wanted)) { pick = o; break; }
		}
		boolean exact = pick != null;
		if (!exact) pick = options.get(0);

		String chosen = optionText(pick);
		js.executeScript("arguments[0].click();", pick);
		Thread.sleep(600);
		return chosen;
	}

	static List<WebElement> typeAndGetOptions(WebElement control, String text) throws Exception {
		WebElement search = waitSearchBox(control, 4);
		if (search == null) throw new IllegalStateException("Dropdown search input container not found");
		search.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
		search.sendKeys(text);
		Thread.sleep(900);

		List<WebElement> options = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			WebElement s = waitSearchBox(control, 1);
			if (s != null) options = getOptions(s);
			if (!options.isEmpty()) break;
			Thread.sleep(300);
		}
		return options;
	}

	static final String OPTIONS_JS =
			"var input = arguments[0];" +
			"var ir = input.getBoundingClientRect();" +
			"function vis(e) { var r = e.getBoundingClientRect(); return r.width > 0 && r.height > 0; }" +
			"function own(d) { var t = ''; for (var k = 0; k < d.childNodes.length; k++) { if (d.childNodes[k].nodeType === 3) t += d.childNodes[k].textContent; } return t.trim(); }" +
			"var boxes = Array.prototype.slice.call(document.querySelectorAll(\"div[style*='max-height'][style*='overflow']\")).filter(vis);" +
			"var best = null, bd = 1e9;" +
			"boxes.forEach(function (c) { var r = c.getBoundingClientRect(); var d = Math.abs(r.top - ir.bottom); if (d < bd) { bd = d; best = c; } });" +
			"var res = [];" +
			"if (best && bd < 150) {" +
			"  Array.prototype.slice.call(best.querySelectorAll('div')).forEach(function (d) { if (vis(d) && own(d) !== '') res.push(d); });" +
			"  return res;" +
			"}" +
			"var all = document.querySelectorAll('div');" +
			"for (var i = 0; i < all.length; i++) {" +
			"  var d = all[i];" +
			"  if (!(input.compareDocumentPosition(d) & 4)) continue;" +
			"  if (!vis(d) || own(d) === '') continue;" +
			"  var r = d.getBoundingClientRect();" +
			"  if (r.top < ir.bottom - 2 || r.top > ir.bottom + 260) continue;" +
			"  if (r.left < ir.left - 30 || r.right > ir.right + 30) continue;" +
			"  var el = document.elementFromPoint(r.left + r.width / 2, r.top + r.height / 2);" +
			"  if (el === d || d.contains(el)) res.push(d);" +
			"}" +
			"return res;";

	@SuppressWarnings("unchecked")
	static List<WebElement> getOptions(WebElement search) {
		List<WebElement> out = new ArrayList<>();
		try {
			List<WebElement> raw = (List<WebElement>) js.executeScript(OPTIONS_JS, search);
			for (WebElement o : raw) {
				String t = optionText(o).toLowerCase();
				if (t.isEmpty() || t.contains("no result") || t.contains("no match") || t.contains("not found") || t.contains("no data")) continue;
				out.add(o);
			}
		} catch (StaleElementReferenceException e) { }
		return out;
	}

	static String optionText(WebElement o) {
		String t = o.getAttribute("textContent");
		return t == null ? "" : clean(t);
	}

	static boolean isExact(String optionText, String wanted) {
		String w = squash(wanted);
		if (squash(optionText).equals(w)) return true;
		int i = optionText.indexOf(" - ");
		if (i > 0) {
			String code = optionText.substring(0, i);
			String name = optionText.substring(i + 3);
			return squash(code).equals(w) || squash(name).equals(w);
		}
		return false;
	}

	static WebElement waitSearchBox(WebElement control, int seconds) throws InterruptedException {
		long end = System.currentTimeMillis() + seconds * 1000L;
		while (System.currentTimeMillis() < end) {
			int cy = 0;
			try { cy = control.getRect().getY(); } catch (StaleElementReferenceException e) { }
			WebElement best = null;
			int bestDist = Integer.MAX_VALUE;
			for (WebElement e : driver.findElements(By.xpath("//input[@placeholder='Search...']"))) {
				try {
					if (!e.isDisplayed()) continue;
					int d = Math.abs(e.getRect().getY() - cy);
					if (d < bestDist) { best = e; bestDist = d; }
				} catch (StaleElementReferenceException ex) { }
			}
			if (best != null) return best;
			Thread.sleep(250);
		}
		return null;
	}

	static void closeDropdown(WebElement control) {
		try {
			WebElement s = waitSearchBox(control, 1);
			if (s != null) {
				s.sendKeys(Keys.ESCAPE);
				Thread.sleep(300);
				if (waitSearchBox(control, 1) != null) js.executeScript("arguments[0].click();", control);
			}
		} catch (Exception ignore) { }
	}

	static void setInput(By by, String value) throws Exception {
		if (value == null || value.isEmpty()) return;
		for (int attempt = 1; attempt <= 3; attempt++) {
			WebElement el = findOne(by);
			scrollCenter(el);
			Thread.sleep(300);
			el.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
			el.sendKeys(value);
			el.sendKeys(Keys.TAB);
			Thread.sleep(400);

			el = findOne(by);
			if (value.equals(el.getAttribute("value"))) return;

			js.executeScript(
					"var el = arguments[0], v = arguments[1];" +
					"Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype,'value').set.call(el, v);" +
					"el.dispatchEvent(new Event('input',{bubbles:true}));" +
					"el.dispatchEvent(new Event('change',{bubbles:true}));" +
					"el.dispatchEvent(new Event('blur',{bubbles:true}));", el, value);
			Thread.sleep(400);
			el = findOne(by);
			if (value.equals(el.getAttribute("value"))) return;
		}
		throw new IllegalStateException("Failed to set value '" + value + "' for element: " + by);
	}

	static void stage(String s) { stage = s; }

	static WebElement findOne(By by) throws InterruptedException {
		long end = System.currentTimeMillis() + 15000;
		while (System.currentTimeMillis() < end) {
			List<WebElement> list = driver.findElements(by);
			if (!list.isEmpty()) return list.get(0);
			Thread.sleep(300);
		}
		throw new NoSuchElementException("Element not found: " + by);
	}

	static By firstExisting(int seconds, By... bys) throws InterruptedException {
		long end = System.currentTimeMillis() + seconds * 1000L;
		while (System.currentTimeMillis() < end) {
			for (By b : bys) {
				if (!driver.findElements(b).isEmpty()) return b;
			}
			Thread.sleep(300);
		}
		throw new NoSuchElementException("Element locator not found: " + bys[0]);
	}

	static int count(By by) { return driver.findElements(by).size(); }

	static void waitCountAtLeast(By by, int atLeast, int seconds, String errorMsg) throws InterruptedException {
		long end = System.currentTimeMillis() + seconds * 1000L;
		while (System.currentTimeMillis() < end) {
			if (count(by) >= atLeast) return;
			Thread.sleep(300);
		}
		throw new IllegalStateException(errorMsg);
	}

	static void scrollCenter(WebElement e) {
		js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", e);
	}

	static void clickSafe(WebElement e) throws InterruptedException {
		scrollCenter(e);
		Thread.sleep(250);
		try {
			e.click();
		} catch (ElementClickInterceptedException ex) {
			js.executeScript("arguments[0].click();", e);
		} catch (ElementNotInteractableException ex) {
			js.executeScript("arguments[0].click();", e);
		}
	}

	static void screenshot(String fileName) {
		try {
			File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Files.copy(f.toPath(), Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Screenshot saved: " + fileName);
		} catch (Exception e) { }
	}

	static String firstLine(String msg) {
		if (msg == null) return "";
		String m = msg.split("\n")[0];
		return m.length() > 400 ? m.substring(0, 400) : m;
	}

	static String clean(String s) {
		if (s == null) return "";
		return s.replace('\u00A0', ' ').replaceAll("\\s+", " ").trim();
	}

	static String low(String s) { return clean(s).toLowerCase(); }

	static String squash(String s) { return clean(s).toLowerCase().replaceAll("\\s+", ""); }

	static void readExcel() throws IOException {
		File file = new File(EXCEL_PATH);
		if (!file.exists()) throw new IOException("Excel file not found at: " + EXCEL_PATH);

		try (FileInputStream fis = new FileInputStream(file); XSSFWorkbook wb = new XSSFWorkbook(fis)) {
			XSSFSheet sheet = wb.getSheet(SHEET_NAME);
			if (sheet == null) throw new IOException("Worksheet not found: " + SHEET_NAME);

			int hdrRow = findRow(sheet, "name of the plan");
			if (hdrRow < 0) throw new IOException("Header row containing 'Name of the Plan' not found");
			XSSFRow hh = sheet.getRow(hdrRow);

			int cName = colExact(hh, "name of the plan");
			int cItem = colStarts(hh, "item code");
			int cQty = colExact(hh, "production quantity");
			int cUnit = colExact(hh, "unit");
			int cOffice = colExact(hh, "office code");
			int cProc = colStarts(hh, "process");
			int cSub = colStarts(hh, "sub");
			int cSfg = colExact(hh, "sfg output item");
			int cOutQty = colExact(hh, "output qty");
			int cOutUnit = colExact(hh, "output unit");
			int cApprox = colExact(hh, "approx. duration");
			int cLabType = colExact(hh, "labour type");
			int cLabDur = colExact(hh, "labour duration");
			int cLabUnit = colExact(hh, "labour duration unit");
			if (cName < 0 || cItem < 0 || cQty < 0 || cUnit < 0 || cOffice < 0 || cProc < 0 || cSub < 0
					|| cSfg < 0 || cOutQty < 0 || cOutUnit < 0) {
				throw new IOException("Required header columns missing in Excel worksheet");
			}

			List<Integer> itemCols = new ArrayList<>();
			for (int c = 0; c < hh.getLastCellNum(); c++) {
				String h = low(getCellValue(hh.getCell(c)));
				if (h.startsWith("item ") && h.contains("for this sfg")) itemCols.add(c);
			}
			if (itemCols.isEmpty()) throw new IOException("No columns matching 'Item 1 (for this SFG)' found");

			boolean planFound = false;
			SubGroup cur = null;

			for (int r = hdrRow + 1; r <= sheet.getLastRowNum(); r++) {
				XSSFRow row = sheet.getRow(r);
				if (row == null || row.getZeroHeight()) continue;

				if (!planFound && !val(row, cName).isEmpty()) {
					plan.name = val(row, cName);
					plan.itemCode = val(row, cItem);
					plan.qty = val(row, cQty);
					plan.unit = val(row, cUnit);
					plan.office = val(row, cOffice);
					planFound = true;
				}

				String process = clean(val(row, cProc));
				String sfgName = val(row, cSfg);
				if (process.isEmpty() && sfgName.isEmpty()) continue;

				if (plan.process.isEmpty()) {
					plan.process = process;
				}

				Line ln = new Line();
				ln.excelRow = r + 1;
				ln.sfg = sfgName;
				ln.qty = val(row, cOutQty);
				ln.unit = val(row, cOutUnit);
				for (int c : itemCols) {
					String v = val(row, c);
					if (!v.isEmpty()) ln.items.add(v);
				}
				ln.approx = val(row, cApprox);
				ln.labType = val(row, cLabType);
				ln.labDur = val(row, cLabDur);
				ln.labUnit = val(row, cLabUnit);

				String sub = val(row, cSub);
				if (cur == null || !cur.key.equals(squash(sub))) {
					cur = new SubGroup(sub);
					subs.add(cur);
				}
				cur.lines.add(ln);
			}
			if (!planFound) throw new IOException("Plan details missing in Excel worksheet");
			if (subs.isEmpty()) throw new IOException("No SFG item rows found in Excel worksheet");
		}
	}

	static void printExcelSummary() {
		System.out.println("Plan Name: " + plan.name + " | Process: " + plan.process);
		int total = 0;
		for (SubGroup sg : subs) {
			System.out.println("   Sub-process: " + sg.name + " -> " + sg.lines.size() + " SFG item(s)");
			total += sg.lines.size();
		}
		System.out.println("Total SFG items to process: " + total + "\n");
	}

	static int findRow(XSSFSheet sheet, String startsWithLower) {
		for (int r = 0; r <= sheet.getLastRowNum(); r++) {
			XSSFRow row = sheet.getRow(r);
			if (row == null) continue;
			for (int c = 0; c < row.getLastCellNum(); c++) {
				if (low(getCellValue(row.getCell(c))).startsWith(startsWithLower)) return r;
			}
		}
		return -1;
	}

	static int colExact(XSSFRow hdr, String exactLower) {
		for (int c = 0; c < hdr.getLastCellNum(); c++) {
			if (low(getCellValue(hdr.getCell(c))).equals(exactLower)) return c;
		}
		return -1;
	}

	static int colStarts(XSSFRow hdr, String startsLower) {
		for (int c = 0; c < hdr.getLastCellNum(); c++) {
			if (low(getCellValue(hdr.getCell(c))).startsWith(startsLower)) return c;
		}
		return -1;
	}

	static String val(XSSFRow row, int col) {
		if (row == null || col < 0) return "";
		return clean(getCellValue(row.getCell(col)));
	}

	static String getCellValue(XSSFCell cell) {
		if (cell == null) return "";
		if (cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue().trim();
		} else if (cell.getCellType() == CellType.NUMERIC) {
			double v = cell.getNumericCellValue();
			return v == (long) v ? String.valueOf((long) v) : String.valueOf(v);
		}
		return cell.toString().trim();
	}
}