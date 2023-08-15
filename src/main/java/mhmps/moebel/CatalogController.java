package mhmps.moebel;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.*;
import static org.salespointframework.core.Currencies.EURO;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

//import static org.salespointframework.core.Currencies.EURO;

//import org.javamoney.moneta.Money;

@Controller
public class CatalogController {

	private MoebelRepository moebelData;
	private BundleRepository bundleData;

	public CatalogController(MoebelRepository moebelData, BundleRepository bundleData) {
		this.moebelData = moebelData;
		this.bundleData = bundleData;

	}

	//// Classes for Moebel

	// Viewing the catalog

	@GetMapping("/moebelnkatalog")
	public String listMoebelnKatalog(Model model) {
		model.addAttribute("moebeln", moebelData.findAll());
		return "Katalog";
	}

	// Viewing the inventory

	@GetMapping("/moebeln")
	@PreAuthorize("isAuthenticated()")
	public String listMoebeln(Model model) {
		model.addAttribute("moebeln", moebelData.findAll());
		return "moebel";
	}

	// Viewing Category
	// Get the category from link and then show the products matching that category
	@GetMapping("/moebelnkatalog/{category}")
	@PreAuthorize("isAuthenticated()")
	String listCategory(@PathVariable String category, Model model) {

		model.addAttribute("moebeln", moebelData.findByType(Category.valueOf(category), Sort.unsorted()));

		return "Katalog";
	}

	// Add new furniture

	@GetMapping("/moebeln/new")
	@PreAuthorize("isAuthenticated()")
	public String createMoebelForm(Model model) {
		Moebel moebel = new Moebel();
		model.addAttribute("moebel", moebel);
		return "create_moebel";

	}

	@PostMapping("/moebeln")
	@PreAuthorize("isAuthenticated()")
	public String saveMoebel(@ModelAttribute("moebel") @Valid Moebel moebel, Errors result,
			@RequestParam("preis") String preis, @RequestParam("category") String category,
			@RequestParam("image") MultipartFile image) {

		// to simplify: this create a BigDecimal num, first by parsing the string
		// "preis" into a Double, then convert it to BigDecimal, then rounding to 2
		// decimal place.
		BigDecimal num = BigDecimal.valueOf(Double.valueOf(preis)).setScale(2, RoundingMode.CEILING);
		// set the price by passing a Money object, with the value of num and the
		// currency Euro
		moebel.setPrice(Money.of(num, EURO));

		// Set annotation for manually inputted items should the field is left blank
		if (moebel.getAnnotation() == null) {
			moebel.setAnnotation(Annotation.Neu);
		}

		if (result.hasErrors()) {
			return "create_moebel";
		}

		moebel.addCategory(category);
		moebelData.save(moebel);
		saveImage(image, moebel.getName());
		return "redirect:/moebeln";
	}

	// Edit existing furniture
	@GetMapping("/moebeln/edit/{id}")
	@PreAuthorize("isAuthenticated()")
	public String editMoebelForm(@PathVariable ProductIdentifier id, Model model) {
		model.addAttribute("moebel", moebelData.findById(id).get());
		return "edit_moebel";
	}

	@PostMapping("/moebeln/{id}")
	@PreAuthorize("isAuthenticated()")
	public String updateMoebel(@PathVariable ProductIdentifier id, @ModelAttribute("moebel") Moebel moebel,
			@RequestParam("preis") String preis, Model model, @RequestParam("image") MultipartFile image) {

		// to simplify: this create a BigDecimal num, first by parsing the string
		// "preis" into a Double, then convert it to BigDecimal, then rounding to 2
		// decimal place.
		BigDecimal num = BigDecimal.valueOf(Double.valueOf(preis)).setScale(2, RoundingMode.CEILING);

		Moebel existingMoebel = moebelData.findById(id).get();
		existingMoebel.setName(moebel.getName());

		// set the price by passing a Money object, with the value of num and the
		// currency Euro
		existingMoebel.setPrice(Money.of(num, EURO));

		existingMoebel.setFarbe(moebel.getFarbe());
		existingMoebel.setMaterial(moebel.getMaterial());
		existingMoebel.setMasse(moebel.getMasse());
		existingMoebel.setHersteller(moebel.getHersteller());
		existingMoebel.setAnnotation(moebel.getAnnotation());
		existingMoebel.setCategory(moebel.getCategory());
		existingMoebel.setStock(moebel.getStock());
		moebelData.save(existingMoebel);
		saveImage(image, moebel.getName());
		return "redirect:/moebeln";
	}
	// Delete an existing furniture

	@GetMapping("/moebeln/{id}")
	@PreAuthorize("isAuthenticated()")
	public String deleteMoebel(@PathVariable ProductIdentifier id) {
		moebelData.deleteById(id);
		return "redirect:/moebeln";
	}

	// View a single item

	//// Bundles

	// Viewing the whole catalog

	@GetMapping("/bundleskatalog")
	// @PreAuthorize("isAuthenticated()")
	public String listBundlesKatalog(Model model) {
		model.addAttribute("bundles", bundleData.findAll());
		return "BundleKatalog";
	}

	// Viewing the inventory

	@GetMapping("/bundles")
	@PreAuthorize("isAuthenticated()")
	public String listBundles(Model model) {
		model.addAttribute("bundles", bundleData.findAll());
		return "bundle";
	}
	// Add new furniture

	@GetMapping("/bundles/new")
	@PreAuthorize("isAuthenticated()")
	public String createBundleForm(Model model) {
		Bundle bundle = new Bundle();
		model.addAttribute("bundle", bundle);
		return "create_bundle";
	}

	@PostMapping("/bundles")
	@PreAuthorize("isAuthenticated()")
	public String saveMoebel(@ModelAttribute("bundle") Bundle bundle, @RequestParam("preis") String preis,
			@RequestParam("image") MultipartFile image) {

		// to simplify: this create a BigDecimal num, first by parsing the string
		// "preis" into a Double, then convert it to BigDecimal, then rounding to 2
		// decimal place.
		BigDecimal num = BigDecimal.valueOf(Double.valueOf(preis)).setScale(2, RoundingMode.CEILING);
		// set the price by passing a Money object, with the value of num and the
		// currency Euro
		bundle.setPrice(Money.of(num, EURO));

		// Set annotation for manually inputted items should the field is left blank
		if (bundle.getAnnotation() == null) {
			bundle.setAnnotation(Annotation.Neu);
		}
		bundleData.save(bundle);
		saveImage(image, bundle.getName());
		return "redirect:/bundles";
	}

	// Edit existing furniture
	@GetMapping("/bundles/edit/{id}")
	@PreAuthorize("isAuthenticated()")
	public String editBundleForm(@PathVariable ProductIdentifier id, Model model) {
		model.addAttribute("bundle", bundleData.findById(id).get());
		return "edit_bundle";
	}

	@PostMapping("/bundles/{id}")
	@PreAuthorize("isAuthenticated()")
	public String updateBundle(@PathVariable ProductIdentifier id, @ModelAttribute("bundle") Bundle bundle,
			@RequestParam("preis") String preis, Model model, @RequestParam("image") MultipartFile image) {

		// to simplify: this create a BigDecimal num, first by parsing the string
		// "preis" into a Double, then convert it to BigDecimal, then rounding to 2
		// decimal place.
		BigDecimal num = BigDecimal.valueOf(Double.valueOf(preis)).setScale(2, RoundingMode.CEILING);

		Bundle existingBundle = bundleData.findById(id).get();
		existingBundle.setName(bundle.getName());

		// set the price by passing a Money object, with the value of num and the
		// currency Euro
		existingBundle.setPrice(Money.of(num, EURO));

		existingBundle.setRabatt(bundle.getRabatt());
		existingBundle.setAnnotation(bundle.getAnnotation());
		existingBundle.setStock(bundle.getStock());
		bundleData.save(existingBundle);
		saveImage(image, bundle.getName());
		return "redirect:/bundles";
	}
	// Delete an existing furniture

	@GetMapping("/bundles/{id}")
	@PreAuthorize("isAuthenticated()")
	public String deleteBundle(@PathVariable ProductIdentifier id) {
		bundleData.deleteById(id);
		return "redirect:/bundles";
	}

	// Adding content into a bundle

	@GetMapping("bundles/view/addcontent/{id}")
	@PreAuthorize("isAuthenticated()")
	public String addContentsForm(@PathVariable ProductIdentifier id, Model model) {
		model.addAttribute("bundle", bundleData.findById(id).get());
		return "addContents";
	}

	@PostMapping("bundles/add/{id}")
	@PreAuthorize("isAuthenticated()")
	public String addContents(@PathVariable ProductIdentifier id, @RequestParam(name = "name") String name) {
		Bundle item = bundleData.findById(id).get();

		// This spaghetti code checks input for 2 conditions
		// - a valid Moebel exists with that name
		// - no Moebel with that name is part of the bundle's content

		if (!moebelData.findByName(name).isEmpty() && !item.findMoebel(moebelData.findByName(name).toList().get(0)))
			item.addMoebel(moebelData.findByName(name).toList().get(0));
		bundleData.save(item);
		return "redirect:/bundles/view/" + item.getId();
	}

	// Removing content from a bundle

	@GetMapping("bundles/view/removecontent/{id}")
	@PreAuthorize("isAuthenticated()")
	public String removeContentsForm(@PathVariable ProductIdentifier id, Model model) {
		model.addAttribute("bundle", bundleData.findById(id).get());
		return "removeContents";
	}

	@PostMapping("bundles/remove/{id}")
	@PreAuthorize("isAuthenticated()")
	public String removeContents(@PathVariable ProductIdentifier id, @RequestParam(name = "name") String name) {
		Bundle item = bundleData.findById(id).get();

		// This spaghetti code checks input for 1 condition
		// a valid Moebel exists with that name
		// possibly redundant, since List.remove() wont change if a non existent item is
		// passed as argument

		if (!moebelData.findByName(name).isEmpty())
			item.removeMoebel(moebelData.findByName(name).toList().get(0));
		bundleData.save(item);
		return "redirect:/bundles/view/" + item.getId();
	}

	// viewing the contents of a bundle

	@GetMapping("/bundles/view/{ID}")
	@PreAuthorize("isAuthenticated()")
	String bundleview(@PathVariable ProductIdentifier ID, Model bundlemodel, Model moebelmodel) {
		Bundle bundle = bundleData.findById(ID).get();
		bundlemodel.addAttribute("bundles", bundle);
		Iterable<Moebel> iterable = bundle.getItemlist();
		moebelmodel.addAttribute("moebeln", iterable);
		return "single_bundle";
	}

	// uploading file
	void saveImage(MultipartFile image, String name) {
		Path path = Paths.get("target/classes/static/resources/img/");
		try {
			
			// Check if string ".jpg" is present in file name
			// can be bypassed by naming the file "file.jpg.png"
			
			if (image.getOriginalFilename().contains(".jpg")) {
				InputStream inputStream = image.getInputStream();
				
				//Save image with the name of Moebel/Bundle.jpg, replace if exists
				Files.copy(inputStream, path.resolve(name + ".jpg"), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
