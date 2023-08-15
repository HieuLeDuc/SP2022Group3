package mhmps.rechnung;

import com.lowagie.text.DocumentException;
import org.salespointframework.order.Order;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.*;
import java.time.format.DateTimeFormatter;

@Service
public class RechnungService {
	//Prefix , Suffix Rechnung Dateien
	public static final String FILE_NAME_PREFIX = "invoice_";
	public static final String FILE_NAME_SUFFIX = ".pdf";
	//Pfad zum Speichern der Rechnungen
	public static String INVOICES_DIRECTORY = System.getProperty("user.home") + File.separator + "invoices"
			+ File.separator;

	private String parseInvoiceTemplateToHtml(Order bestellung) {
		//thymeleaf resolver konfigurieren
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setSuffix(".html");
		resolver.setPrefix("templates/");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setOrder(0);

		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(resolver);
		//Variablen die im Template verwendet werden
		Context context = new Context();
		context.setVariable("order", bestellung);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		context.setVariable("orderDateTime", bestellung.getDateCreated().format(formatter));

		return engine.process("rechnung", context);
	}

	private void SaveInvoiceHtmlAsPdf(String html, String invoiceRef) throws DocumentException, IOException {
		
		//Datei Name und Pfad initialisieren
		String fileName = FILE_NAME_PREFIX + invoiceRef + FILE_NAME_SUFFIX;
		String outputFolder = INVOICES_DIRECTORY + fileName;

		File dir = new File(INVOICES_DIRECTORY);
		if (!dir.exists())
			dir.mkdirs();

		OutputStream outputStream = new FileOutputStream(outputFolder);
		//PDF erstellen
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(html);
		renderer.layout();
		renderer.createPDF(outputStream);

	}
	// Funktion, die alle oben genannten Prozesse aufruft (Und übergeben diese an die Funktion, die sie als Pdf speichert)
	public void getInvoice(Order bestellung) throws DocumentException, IOException {
		SaveInvoiceHtmlAsPdf(parseInvoiceTemplateToHtml(bestellung), bestellung.getId().getIdentifier());
	}

}
