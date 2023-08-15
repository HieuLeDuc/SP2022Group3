package mhmps.rechnung;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Controller
@RequestMapping("/rechnung")
public class RechnungController {

	//Rechnung generieren durch angabe von OrderRef
	@GetMapping("/download-invoice")
	public Object downloadInvoice(@RequestParam String orderRef) {
	//Erstellen File unter Verwendung des Pfads der Rechnung, die von der OrderRedf erstellt wurde .
		File file = new File(RechnungService.INVOICES_DIRECTORY + RechnungService.FILE_NAME_PREFIX + orderRef
				+ RechnungService.FILE_NAME_SUFFIX);
		InputStreamResource resource = null;
		try {
			resource = new InputStreamResource(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			System.out.println("INVOICE NOT FOUND (Not exist or Invalid path)");
			return "redirect:/bestellung";
		}
		//Stellen wir die Datei dem Frontend-Benutzer bereit, damit sie heruntergeladen werden kann
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length()).body(resource);
	}

}
