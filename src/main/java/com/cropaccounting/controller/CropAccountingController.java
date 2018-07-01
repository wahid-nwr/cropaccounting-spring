package com.cropaccounting.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cropaccounting.beans.ExpenditureRequest;
import com.cropaccounting.beans.ExpenditureResponse;
import com.cropaccounting.models.Crop;
import com.cropaccounting.models.CropActivity;
import com.cropaccounting.models.CropActivityItem;
import com.cropaccounting.models.CropActivityType;
import com.cropaccounting.models.CropCalenderTask;
import com.cropaccounting.models.CropExpenceList;
import com.cropaccounting.models.CropIncome;
import com.cropaccounting.models.CropIncomeItem;
import com.cropaccounting.models.CropIncomeList;
import com.cropaccounting.models.CropTaskMap;
import com.cropaccounting.models.Crops;
import com.cropaccounting.models.ExpenceItem;
import com.cropaccounting.models.ExpenceItemValue;
import com.cropaccounting.models.FarmerCropTask;
import com.cropaccounting.models.FarmerTask;
import com.cropaccounting.models.IncomeItem;
import com.cropaccounting.models.IncomeItemValue;
import com.cropaccounting.models.UserModel;
import com.cropaccounting.models.Varieties;
import com.cropaccounting.service.CropAccountingService;

@Controller
public class CropAccountingController {
	private final Logger log = LoggerFactory.getLogger(CropAccountingController.class);
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private CropAccountingService cropAccountingService;

	@PostMapping(value = "/api/expenditure")
	@ResponseBody
	public ExpenditureResponse extractManifest(@RequestBody ExpenditureRequest req, Locale lc) {
		return ExpenditureResponse.newInstance();
	}

	@RequestMapping("/cropmanagement/createcropprice")
	public List<UserModel> createcropprice() {
		return cropAccountingService.getUsersExceptAdmin();
	}

	@RequestMapping("/cropmanagement/createitemprice")
	public List<UserModel> createitemprice() {
		return cropAccountingService.getUsersExceptAdmin();
	}

	@RequestMapping("/cropmanagement/cropcalender")
	public void cropcalender(@RequestParam("nid") String nid, Model model) {
		// String nid = params.get("nid");
		List<UserModel> users = cropAccountingService.getUsersExceptAdmin();
		/*
		 * Jedis j =
		 * play.Play.application().plugin(RedisPlugin.class).jedisPool().getResource();
		 * try { //All messages are pushed through the pub/sub channel
		 * j.publish(ChatRoom.CHANNEL, Json.stringify(Json.toJson(talk))); } finally {
		 * play.Play.application().plugin(RedisPlugin.class).jedisPool().returnResource(
		 * j); }
		 */
		// play.modules.redis.Redis.set("test",users.toString());
		/*Cache.add("testModelObject", users);
		// System.out.println(Cache.get("testModelObject"));
		users = (List<UserModel>) Cache.get("testModelObject");*/
		// TestModelObject obj = Cache.get("testModelObject", TestModelObject.class);
		// play.modules.redis.Redis.set("users",users);
		// List<Crop> crops = Crop.find(" farmer.nid = '22'").fetch();
		List<FarmerCropTask> farmerCropTaskList = cropAccountingService.getFarmerCropTaskById(nid);
		// System.out.println("farmerCropTaskList:::"+farmerCropTaskList);
		// render(users, farmerCropTaskList, nid);
		model.addAttribute("nid", nid);
		model.addAttribute("farmerCropTaskList", farmerCropTaskList);
		model.addAttribute("users", users);
	}

	@RequestMapping("/cropmanagement/updatetaskdate")
	public void updatetaskdate(@RequestParam("start") String start, @RequestParam("end") String end,
			@RequestParam("taskId") String taskId) {
		System.out.println("taskId:::" + taskId + "    start::" + start + " end:::" + end);
		Optional<FarmerTask> taskOptional = cropAccountingService.getFarmerTaskById(Long.parseLong(taskId));
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern("yyyy-MM-dd HH:mm:ss");

		FarmerTask task = null;
		if (!taskOptional.isPresent())
			throw new NullPointerException();

		task = taskOptional.get();
		task.setStart(java.time.LocalDateTime.parse(start, formatter));
		task.setEnd(java.time.LocalDateTime.parse(end, formatter));

		cropAccountingService.saveFarmerTask(task);
		// //render(users, farmerCropTaskList, nid);
	}

	@RequestMapping("/cropmanagement/farmercropearning")
	public void farmercropearning(@RequestParam("nid") String nid, Model model) {
		nid = nid != null ? nid : "22";
		// List<UserModel> users = UserModel.find("id <> 1").fetch();
		LocalDate startDate = LocalDate.now();
		startDate = startDate.minusMonths(6);
		System.out.println("startDate::" + startDate);
		List<Crop> crops = cropAccountingService.getCropByNID(nid, startDate);
		System.out.println("crops::" + crops);
		Map<String, List<CropIncomeList>> incomeMap = new HashMap<>();
		for (Crop crop : crops) {
			incomeMap.put("" + crop.getId(),
					cropAccountingService.getCropIncomeLists(crop.getType(), crop.getVarity(), crop.getCrop()));
		}
		System.out.println("incomeMap:::" + incomeMap);
		// render(users, crops, incomeMap, nid);
		model.addAttribute("nid", nid);
		model.addAttribute("incomeMap", incomeMap);
		model.addAttribute("crops", crops);
	}

	@RequestMapping("/cropmanagement/cropcalender1")
	public void cropcalender1() {
		//// List<UserModel> users = UserModel.find("id <> 1").fetch();
		// render(users);
	}

	@RequestMapping("/cropmanagement/cropcalender2")
	public void cropcalender2() {
		// List<UserModel> users = UserModel.find("id <> 1").fetch();
		// render(users);
	}

	@RequestMapping("/cropmanagement/croplist")
	public void croplist(Model model) {
		// List<UserModel> users = UserModel.find("id <> 1").fetch();
		// Connection conn = play.db.DB.getDBConfig("other").getConnection();
		//javax.persistence.EntityManager em = play.db.jpa.JPA.em("other");
		// Query q
		@SuppressWarnings("unchecked")
		List<Object[]> types = em.createNativeQuery("SELECT distinct a.type FROM crops a").getResultList();
		@SuppressWarnings("unchecked")
		List<Object[]> crops = em.createNativeQuery("SELECT  a.type,a.name,a.id FROM crops a").getResultList();
		String query = "SELECT a.crop_id,a.name, a.local_name, a.spec,a.production_with_irrigation,a.production_without_irrigation,"
				+ "a.life_time, a.special FROM varieties a where crop_id is not null and deleted_at is null";
		query = "SELECT a.crop_id,a.name FROM varieties a where crop_id is not null";
		System.out.println("query::::" + query);
		@SuppressWarnings("unchecked")
		List<Object[]> varities = em.createNativeQuery(query).getResultList();
		// render(types, crops, varities);
		model.addAttribute("types", types);
		model.addAttribute("crops", crops);
		model.addAttribute("varities", varities);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/cropmanagement/createcrop")
	public void createcrop(@RequestParam("cropId") String cropId, @RequestParam("cropType") String cropType, Model model) {
		//javax.persistence.EntityManager em = play.db.jpa.JPA.em("other");
		Object[] crop = null;
		List<Object[]> crops = null;
		if (cropId != null && !cropId.equals("null") && cropId.length() > 0) {
			crops = em.createNativeQuery("SELECT  a.id,a.type,a.name FROM crops a where a.id = " + cropId)
					.getResultList();
			if (!crops.isEmpty())
				crop = crops.get(0);
		}
		// System.out.println("crop::::"+crop[0]);
		// render(crops, cropType);
		model.addAttribute("crops", crops);
		model.addAttribute("cropType", cropType);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/cropmanagement/submitCrop")
	public String submitCrop(@RequestParam("cropId") String cropId, @RequestParam("cropType") String cropType,
			@RequestParam("crop_name") String cropName, @RequestParam("varityName") String varityName, Model model) {
		/*
		 * validation.valid(crop); if(Validation.hasErrors()) {
		 * flash.error("Customer "+crop.name+" could not be saved!Error="+Validation.
		 * errors().toString()); Logger.info("Error!!  "+
		 * Validation.errors().toString()); //render("@createcrop", crop); }
		 * crop.save(); List<Crop> cropList = Crop.findAll();
		 */
		//javax.persistence.EntityManager em = play.db.jpa.JPA.em("other");
		// List<Object[]> crops = null;
		if (cropId != null && !cropId.equals("null") && cropId.length() > 0) {
			// javax.persistence.EntityTransaction et = em.getTransaction();
			// et.begin();
			String query = "insert into varieties(name,crop_id) values('" + varityName + "'," + cropId
					+ ");SELECT LAST_INSERT_ID();";
			System.out.println("query::::" + query);
			em.createNativeQuery(query).executeUpdate();
			// et.commit();
		} else {
			/*
			 * String query =
			 * "insert into crops(name,type,image) values('"+crop_name+"','"+cropType+
			 * "','');"; System.out.println("query::::"+query); int value =
			 * em.createNativeQuery(query).executeUpdate();
			 * 
			 * query =
			 * "insert into varieties(name,crop_id) values('"+varityName+"',"+cropId+");";
			 * System.out.println("query::::"+query);
			 * //em.createNativeQuery(query).executeUpdate();
			 */
			Crops portalcrops = new Crops();
			portalcrops.setName(cropName);
			portalcrops.setType(cropType);
			cropAccountingService.saveCrops(portalcrops);

			Varieties protalvirieties = new Varieties();
			protalvirieties.setName(varityName);
			protalvirieties.setCrop_id(portalcrops.getId());
			
			cropAccountingService.saveVarieties(protalvirieties);
			System.out.println("crops::::" + protalvirieties.getId());
		}
		// //javax.persistence.EntityManager em = play.db.jpa.JPA.em("other");
		// Query q
		List<Object[]> types = em.createNativeQuery("SELECT distinct a.type FROM crops a").getResultList();
		List<Object[]> crops = em.createNativeQuery("SELECT  a.type,a.name,a.id FROM crops a").getResultList();
		String query = "SELECT a.crop_id,a.name, a.local_name, a.spec,a.production_with_irrigation,a.production_without_irrigation,"
				+ "a.life_time, a.special FROM varieties a where crop_id is not null and deleted_at is null";
		query = "SELECT a.crop_id,a.name FROM varieties a where crop_id is not null";
		System.out.println("query::::" + query);
		List<Object[]> varities = em.createNativeQuery(query).getResultList();
		// render("@croplist", types, crops, varities);
		// //render();
		model.addAttribute("types", types);
		model.addAttribute("crops", crops);
		model.addAttribute("varities", varities);
		return "redirect:/cropmanagement/croplist";
	}

	@RequestMapping("/cropmanagement/list")
	public void list(Model model) {
		List<Crop> cropList = cropAccountingService.getCropList();
		// render(cropList);
		model.addAttribute("cropList", cropList);
	}

	@RequestMapping("/cropmanagement/createActivity")
	public void createActivity() {
		// List<UserModel> users = UserModel.find("id <> 1").fetch();
		// render(users);
	}

	@RequestMapping("/cropmanagement/createActivityType")
	public void createActivityType() {
		// List<UserModel> users = UserModel.find("id <> 1").fetch();
		// render(users);
	}

	@RequestMapping("/cropmanagement/createIncome")
	public void createIncome() {
		// List<UserModel> users = UserModel.find("id <> 1").fetch();
		// render(users);
	}

	@PostMapping("/cropmanagement/submitIncomeByDate")
	public String submitIncomeByDate(@RequestBody CropIncomeList cropIncomeList, @RequestParam("type") String type,
			@RequestParam("crop") String cropIdStr, @RequestParam("varity") String varity, 
			@RequestParam("income") String[] cropIncomeItems,	@RequestParam("items") String[] items,
			@RequestParam("day") String[] days, @RequestParam("amounts") String[] amounts,
			@RequestParam("values") String[] values, Model model) {
		System.out.println("type::" + type + "   cropIdStr::" + cropIdStr + "    varity::" + varity);
		cropIncomeList.setType(type.split(":")[1]);
		if (cropIdStr != null) {
			cropIncomeList.setCrop(Long.parseLong(cropIdStr.split(":")[1]));
			Optional<Crops> protalCrops = cropAccountingService.getCropsById(cropIncomeList.getCrop());
			if (protalCrops.isPresent())
				cropIncomeList.setCropName(protalCrops.get().getName());
		}
		if (varity != null) {
			cropIncomeList.setVarity(Long.parseLong(varity.split(":")[1]));
			Optional<Varieties> protalVarity = cropAccountingService.getVarietiesById(cropIncomeList.getVarity());
			if (protalVarity.isPresent())
				cropIncomeList.setVarityName(protalVarity.get().getName());
		}

		Optional<CropIncomeList> repoCropIncomeList = cropAccountingService.getCropIncomeList(cropIncomeList.getType(),
				cropIncomeList.getCrop(), cropIncomeList.getVarity());
		// validation.valid(cropIncomeList);

		IncomeItemValue incomeItemValue = null;
		Optional<CropIncome> cropIncome = null;
		List<IncomeItemValue> incomeItemValueList = new ArrayList<>();
		// String type = "";
		for (int i = 1; cropIncomeItems != null && i < cropIncomeItems.length; i++) {
			incomeItemValue = new IncomeItemValue();

			if (cropIncomeItems[i] != null && cropIncomeItems[i].length() > 0) {
				System.out.println("cropIncomeItems::::::" + cropIncomeItems[i]);
				String id = cropIncomeItems[i].split("_")[1];
				cropIncome = cropAccountingService.getCropIncomeById(Long.parseLong(id));
				if (cropIncome.isPresent())
					incomeItemValue.setCropIncome(cropIncome.get());
			}
			incomeItemValue.setType(items[i]);
			if (days[i] != null && days[i].length() > 0)
				incomeItemValue.setDay(Integer.parseInt(days[i]));
			// if(amounts[i]!=null && amounts[i].length()>0)
			// incomeItemValue.amount = Float.parseFloat(amounts[i]);
			if (values[i] != null && values[i].length() > 0)
				incomeItemValue.setTotValue(Float.parseFloat(values[i]));
			System.out.println("incomeItemValue::" + incomeItemValue);

			incomeItemValueList.add(incomeItemValue);
		}
		if (repoCropIncomeList.isPresent()) {
			// repoCropIncomeList.incomeItemValueList.removeAll(repoCropIncomeList.incomeItemValueList);
			repoCropIncomeList.get().getIncomeItemValueList().clear();
			// repoCropIncomeList.save();
			// cropIncomeList.save();
			// cropIncomeList.id = repoCropIncomeList.id;
			// cropIncomeList.incomeItemValueList = incomeItemValueList;
			repoCropIncomeList.get().getIncomeItemValueList().addAll(incomeItemValueList);
			cropAccountingService.saveCropIncomeList(repoCropIncomeList.get());
		} else {
			cropAccountingService.saveCropIncomeList(cropIncomeList);
		}
		/*
		 * if(Validation.hasErrors()) {
		 * flash.error("Customer "+cropIncomeList.id+" could not be saved!Error="
		 * +Validation.errors().toString()); Logger.info("Error!!  "+
		 * Validation.errors().toString()); javax.persistence.EntityManager em =
		 * play.db.jpa.JPA.em("other"); List<Object[]> types =
		 * em.createNativeQuery("SELECT distinct a.type FROM crops a").getResultList();
		 * List<Object[]> crops =
		 * em.createNativeQuery("SELECT  a.type,a.name,a.id FROM crops a").getResultList
		 * (); String query =
		 * "SELECT a.crop_id,a.name, a.local_name, a.spec,a.production_with_irrigation,a.production_without_irrigation,"
		 * +"a.life_time, a.special FROM varieties a where crop_id is not null and deleted_at is null"
		 * ; System.out.println("query::::"+query); List<Object[]> varities =
		 * em.createNativeQuery(query).getResultList(); List<CropIncome> cropIncomes =
		 * CropIncome.findAll(); List<IncomeItem> incomeItemList = IncomeItem.findAll();
		 * List<CropIncomeList> cropIncomesList = CropIncomeList.findAll();
		 * //render("@createearnings", cropIncomes,
		 * cropIncomesList,incomeItemList,types,crops,varities); }
		 */
		// cropIncomeList.type = cropTaskMap.type;
		// cropIncomeList.crop = cropTaskMap.crop;
		// cropIncomeList.varity = cropTaskMap.varity;

		List<CropIncomeList> cropIncomesList = cropAccountingService.getCropIncomeLists();
		// render("@createearnings", cropIncomesList);
		model.addAttribute("cropIncomesList", cropIncomesList);
		return "redirect:/cropmanagement/createearnings";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/cropmanagement/createearnings")
	public void createearnings(Model model) {
		//javax.persistence.EntityManager em = play.db.jpa.JPA.em("other");
		//List<Crop> cropList = cropAccountingService.getCropList();
		
		List<Object[]> types = em.createNativeQuery("SELECT distinct a.type FROM crops a").getResultList();
		List<Object[]> crops = em.createNativeQuery("SELECT  a.type,a.name,a.id FROM crops a").getResultList();
		String query = "SELECT a.crop_id,a.name, a.local_name, a.spec,a.production_with_irrigation,a.production_without_irrigation,"
				+ "a.life_time, a.special FROM varieties a where crop_id is not null and deleted_at is null";
		query = "SELECT a.crop_id,a.name FROM varieties a where crop_id is not null";
		System.out.println("query::::" + query);
		List<Object[]> varities = em.createNativeQuery(query).getResultList();
		List<CropIncome> cropIncomes = cropAccountingService.getCropIncomeList();
		List<IncomeItem> incomeItemList = cropAccountingService.getIncomeItemList();
		List<CropIncomeList> cropIncomesList = cropAccountingService.getCropIncomeLists();
		// render(cropIncomes, cropIncomesList, incomeItemList, types, crops, varities);
		model.addAttribute("cropIncomes", cropIncomes);
		model.addAttribute("cropIncomesList", cropIncomesList);
		model.addAttribute("incomeItemList", incomeItemList);
		model.addAttribute("types", types);
		model.addAttribute("crops", crops);
		model.addAttribute("varities", varities);
	}

	@RequestMapping("/cropmanagement/createIncomeItem")
	public void createIncomeItem() {
		// List<UserModel> users = UserModel.find("id <> 1").fetch();
		// render(users);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/cropmanagement/submitIncome")
	public String submitIncome(@ModelAttribute CropIncome cropIncome, Model model) {
		// validation.valid(cropIncome);
		/*
		 * if (Validation.hasErrors()) { flash.error("Customer " + cropIncome.name +
		 * " could not be saved!Error=" + Validation.errors().toString());
		 * Logger.info("Error!!  " + Validation.errors().toString());
		 * //render("@createincome", cropIncome); }
		 */
		cropAccountingService.saveCropIncome(cropIncome);
		//javax.persistence.EntityManager em = play.db.jpa.JPA.em("other");
		
		List<Object[]> types = em.createNativeQuery("SELECT distinct a.type FROM crops a").getResultList();
		List<Object[]> crops = em.createNativeQuery("SELECT  a.type,a.name,a.id FROM crops a").getResultList();
		String query = "SELECT a.crop_id,a.name, a.local_name, a.spec,a.production_with_irrigation,a.production_without_irrigation,"
				+ "a.life_time, a.special FROM varieties a where crop_id is not null and deleted_at is null";
		query = "SELECT a.crop_id,a.name FROM varieties a where crop_id is not null";
		System.out.println("query::::" + query);
		List<Object[]> varities = em.createNativeQuery(query).getResultList();
		List<CropIncome> incomeList = cropAccountingService.getCropIncomeList();
		// render("@createearnings", incomeList, types, crops, varities);
		model.addAttribute("incomeList", incomeList);
		model.addAttribute("types", types);
		model.addAttribute("crops", crops);
		model.addAttribute("varities", varities);
		return "redirect:/cropmanagement/createearnings";
	}

	@PostMapping("/cropmanagement/submitIncomeItem")
	public String submitIncomeItem(@ModelAttribute CropIncomeItem cropIncomeItem, Model model) {
		/*
		 * validation.valid(cropIncomeItem); if (Validation.hasErrors()) { flash.error(
		 * "Customer " + cropIncomeItem.name + " could not be saved!Error=" +
		 * Validation.errors().toString()); Logger.info("Error!!  " +
		 * Validation.errors().toString()); //render("@createincomeitem",
		 * cropIncomeItem); }
		 */
		cropAccountingService.saveCropIncomeItem(cropIncomeItem);
		List<CropIncomeItem> incomeItemList = cropAccountingService.getCropIncomeItemList();
		// render("@incomeitemlist", incomeItemList);
		model.addAttribute("incomeItemList", incomeItemList);
		return "redirect:/cropmanagement/incomeitemlist";
	}

	@RequestMapping("/cropmanagement/createActivityItem")
	public void createActivityItem() {
		// List<UserModel> users = UserModel.find("id <> 1").fetch();
		// render(users);
	}

	@PostMapping("/cropmanagement/submitActivity")
	public String submitActivity(@ModelAttribute CropActivity cropActivity, Model model) {
		/*
		 * validation.valid(cropActivity); if (Validation.hasErrors()) { flash.error(
		 * "Customer " + cropActivity.name + " could not be saved!Error=" +
		 * Validation.errors().toString()); Logger.info("Error!!  " +
		 * Validation.errors().toString()); //render("@activitylist", cropActivity); }
		 */
		cropAccountingService.saveCropActivity(cropActivity);
		List<CropActivity> activityList = cropAccountingService.getCropActivityList();
		// render("@activitylist", activityList);
		model.addAttribute("activityList", activityList);
		return "redirect:/cropmanagement/activityList";
	}

	@PostMapping("/cropmanagement/submitActivityType")
	public String submitActivityType(@ModelAttribute CropActivityType cropActivityType, Model model) {
		/*
		 * validation.valid(cropActivityType); if (Validation.hasErrors()) {
		 * flash.error("Customer " + cropActivityType.name +
		 * " could not be saved!Error=" + Validation.errors().toString());
		 * Logger.info("Error!!  " + Validation.errors().toString());
		 * //render("@activityTypeList", cropActivityType); }
		 */
		cropAccountingService.saveCropActivityType(cropActivityType);
		List<CropActivityType> activityList = cropAccountingService.getCropActivityTypeList();
		// render("@activityTypeList", activityList);
		model.addAttribute("activityList", activityList);
		return "redirect:/cropmanagement/activityTypeList";
	}

	@PostMapping("/cropmanagement/submitActivityItem")
	public String submitActivityItem(@ModelAttribute CropActivityItem cropActivityItem, Model model) {
		/*
		 * validation.valid(cropActivityItem); if (Validation.hasErrors()) {
		 * flash.error("Customer " + cropActivityItem.name +
		 * " could not be saved!Error=" + Validation.errors().toString());
		 * Logger.info("Error!!  " + Validation.errors().toString());
		 * //render("@activityItemList", cropActivityItem); }
		 */
		cropAccountingService.saveCropActivityItem(cropActivityItem);
		List<CropActivityItem> cropActivityItemList = cropAccountingService.getCropActivityItemList();
		// render("@activityItemList", cropActivityItemList);
		model.addAttribute("cropActivityItemList", cropActivityItemList);
		return "redirect:/cropmanagement/activityItemList";
	}

	@RequestMapping("/cropmanagement/activitylist")
	public void activitylist(Model model) {
		List<CropActivity> activityList = cropAccountingService.getCropActivityList();
		// render(activityList);
		model.addAttribute("activityList", activityList);
	}

	@RequestMapping("/cropmanagement/editActivity")
	public String editActivity(Long id, Model model) {
		Optional<CropActivity> cropActivity = cropAccountingService.getCropActivity(id);
		// render("@createActivity", cropActivity);
		model.addAttribute("cropActivity", cropActivity);
		return "redirect:/cropmanagement/createActivity";
	}

	@RequestMapping("/cropmanagement/activityTypeList")
	public void activityTypeList(Model model) {
		List<CropActivityType> activityTypeList = cropAccountingService.getCropActivityTypeList();
		// render(activityTypeList);
		model.addAttribute("activityTypeList", activityTypeList);
	}

	@RequestMapping("/cropmanagement/editActivityType")
	public String editActivityType(Long id, Model model) {
		Optional<CropActivityType> cropActivityType = cropAccountingService.getCropActivityType(id);
		// render("@createActivityType", cropActivityType);
		model.addAttribute("cropActivityType", cropActivityType);
		return "redirect:/cropmanagement/createActivity";
	}

	@RequestMapping("/cropmanagement/activityItemList")
	public void activityItemList(Model model) {
		List<CropActivityItem> activityItemList = cropAccountingService.getCropActivityItemList();
		// render(activityItemList);
		model.addAttribute("activityItemList", activityItemList);
	}

	@RequestMapping("/cropmanagement/editActivityItem")
	public String editActivityItem(Long id, Model model) {
		Optional<CropActivityItem> cropActivityItem = cropAccountingService.getCropActivityItem(id);
		// render("@createActivityItem", cropActivityItem);
		model.addAttribute("cropActivityItem", cropActivityItem);
		return "redirect:/cropmanagement/createActivity";
	}

	@RequestMapping("/cropmanagement/createmaincrop")
	public void createmaincrop() {
		// List<UserModel> users = UserModel.find("id <> 1").fetch();
		// render(users);
	}

	@RequestMapping("/cropmanagement/cropmatrix")
	public void cropmatrix() {
		// List<UserModel> users = UserModel.find("id <> 1").fetch();
		// render(users);
	}

	@RequestMapping("/cropmanagement/updatecropprice")
	public void updatecropprice() {
		// List<UserModel> users = UserModel.find("id <> 1").fetch();
		// render(users);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/cropmanagement/createcropcalendar")
	public void createcropcalendar(Model model) {
		//javax.persistence.EntityManager em = play.db.jpa.JPA.em("other");
		//List<Crop> cropList = cropAccountingService.getCropList();
		List<Object[]> types = em.createNativeQuery("SELECT distinct a.type FROM crops a").getResultList();
		
		List<Object[]> crops = em.createNativeQuery("SELECT  a.type, a.name, a.id FROM crops a").getResultList();
		String query = "SELECT a.crop_id,a.name, a.local_name, a.spec,a.production_with_irrigation,a.production_without_irrigation,"
				+ "a.life_time, a.special FROM varieties a where crop_id is not null and deleted_at is null";
		query = "SELECT a.crop_id, a.name FROM varieties a where crop_id is not null";
		System.out.println("query::::" + query);
		List<Object[]> varieties = em.createNativeQuery(query).getResultList();
		//System.out.println("varities::" + varities);
		List<CropActivity> cropActivityList = cropAccountingService.getCropActivityList();
		List<ExpenceItem> expenceItemList = cropAccountingService.getExpenceItemList();
		// render(cropActivityList, expenceItemList, types, crops, varities);
		model.addAttribute("cropActivityList", cropActivityList);
		model.addAttribute("expenceItemList", expenceItemList);
		model.addAttribute("types", types);
		model.addAttribute("crops", crops);
		model.addAttribute("varieties", varieties);
	}

	@RequestMapping("/cropmanagement/submitCropCalTask")
	public void submitCropCalTask(@ModelAttribute CropTaskMap cropTaskMap, @RequestParam("cropTaskMap.type") String type,
			@RequestParam("cropTaskMap.crop") String crop, @RequestParam("cropTaskMap.varity") String varity,
			@RequestParam("taskname") String[] replaceNames,	@RequestParam("taskdate") String[] replaceDates,
			@RequestParam("activity") String[] replaceAct, @RequestParam("task") String[] replaceTask,
			@RequestParam("comments") String[] replaceComm, Model model) {
		/*
		 * validation.valid(cropTask); if(Validation.hasErrors()) {
		 * flash.error("Customer "+cropTask.taskName+" could not be saved!Error="
		 * +Validation.errors().toString()); Logger.info("Error!!  "+
		 * Validation.errors().toString()); //render("@createcropcalendar", cropTask); }
		 * cropTask.save(); List<CropCalenderTask> cropTaskList =
		 * CropCalenderTask.findAll();
		 */

		type = type.replace("string:", "");
		crop = crop.replace("string:", "");
		varity = varity.replace("string:", "");
		System.out.println("type:::" + type + " crop::" + crop + " varity::" + varity);

		if (!type.isEmpty())
			cropTaskMap.setType(type);
		if (!crop.isEmpty())
			cropTaskMap.setCrop(Long.parseLong(crop));
		if (!varity.isEmpty())
			cropTaskMap.setVarity(Long.parseLong(varity));

		List<CropCalenderTask> taskList = new ArrayList<CropCalenderTask>();

		CropCalenderTask cropCalenderTask = null;
		Optional<CropActivity> cropActivity = null;
		Optional<CropActivityType> cropActivityType = null;
		for (int i = 1; replaceDates != null && i < replaceDates.length; i++) {
			cropCalenderTask = new CropCalenderTask();
			if (replaceDates[i] != null && replaceDates[i].length() > 0)
				cropCalenderTask.setTaskDateStr(replaceDates[i]);
			if (replaceNames != null && replaceNames[i] != null && replaceNames[i].length() > 0)
				cropCalenderTask.setTaskName(replaceNames[i]);
			System.out.println("replaceAct[i]::" + replaceAct[i]);
			if (replaceAct[i] != null && replaceAct[i].length() > 0) {
				cropActivity = cropAccountingService.getCropActivity(Long.parseLong(replaceAct[i]));
				if (cropActivity.isPresent())
					cropCalenderTask.setCropActivity(cropActivity.get());
			}
			System.out.println("replaceTask[i]::" + replaceTask[i]);
			if (replaceTask[i] != null && replaceTask[i].length() > 0) {
				cropActivityType = cropAccountingService.getCropActivityType(Long.parseLong(replaceTask[i]));
				if (cropActivityType.isPresent())
					cropCalenderTask.setCropActivityType(cropActivityType.get());
			}
			if (replaceComm[i] != null && replaceComm[i].length() > 0)
				cropCalenderTask.setComments(replaceComm[i]);

			taskList.add(cropCalenderTask);
		}
		cropTaskMap.setTaskList(taskList);
		cropAccountingService.saveCropTaskMap(cropTaskMap);
		//List<CropTaskMap> cropTaskMapList = cropAccountingService.getCropTaskMapList();
		// //render("@cropTaskMaps",cropTaskMaps);
		createTaskExpenditure(cropTaskMap.getId(), model);
	}

	@RequestMapping("/cropmanagement/cropTasklist")
	public void cropTasklist(Model model) {
		List<CropTaskMap> cropTaskMapList = cropAccountingService.getCropTaskMapList();
		// render(cropTaskMapList);
		model.addAttribute("cropTaskMapList", cropTaskMapList);
	}

	@RequestMapping("/cropmanagement/createTaskExpenditure")
	public void createTaskExpenditure(Long id, Model model) {
		System.out.println("id:::" + id);
		Optional<CropTaskMap> cropTaskMap = cropAccountingService.getCropTaskMap(id);
		// List<CropActivity> cropActivityList = CropActivity.findAll();
		// List<CropActivityType> cropActivityTypeList =
		// CropActivityType.findAll();
		List<ExpenceItem> expenceItemList = cropAccountingService.getExpenceItemList();
		model.addAttribute("cropTaskMap", cropTaskMap);
		model.addAttribute("expenceItemList", expenceItemList);
		// render(cropTaskMap, expenceItemList);
	}

	@RequestMapping("/cropmanagement/cropexpencelist")
	public void cropexpencelist(Model model) {
		List<CropExpenceList> cropExpences = cropAccountingService.getCropExpenceLists();
		model.addAttribute("cropExpences", cropExpences);
		// render(cropExpences);
	}

	@RequestMapping("/cropmanagement/createExpenceItem")
	public void createExpenceItem(Model model) {
		List<CropActivity> cropActivityList = cropAccountingService.getCropActivityList();
		List<CropActivityType> cropActivityTypeList = cropAccountingService.getCropActivityTypelist();
		model.addAttribute("cropActivityList", cropActivityList);
		model.addAttribute("cropActivityTypeList", cropActivityTypeList);
		// render(cropActivityList, cropActivityTypeList);
	}

	@RequestMapping("/cropmanagement/submitExpenceItem")
	public void submitExpenceItem(@ModelAttribute ExpenceItem expenceItem, @RequestParam("cropActivity") String cropActivityId,
			@RequestParam("cropActivityType") String cropActivityTypeId,
			@RequestParam("cropActivityItem") String cropActivityItemId) {
		expenceItem = new ExpenceItem();
		Optional<CropActivity> cropActivity = null;
		Optional<CropActivityType> cropActivityType = null;
		Optional<CropActivityItem> cropActivityItem = null;

		if (!cropActivityId.isEmpty())
			cropActivity = cropAccountingService.getCropActivity(Long.parseLong(cropActivityId));
		if (!cropActivityTypeId.isEmpty())
			cropActivityType = cropAccountingService.getCropActivityType(Long.parseLong(cropActivityTypeId));
		if (!cropActivityItemId.isEmpty())
			cropActivityItem = cropAccountingService.getCropActivityItem(Long.parseLong(cropActivityItemId));

		if (cropActivity.isPresent())
			expenceItem.setCropActivity(cropActivity.get());
		if (cropActivityType.isPresent())
			expenceItem.setCropActivityType(cropActivityType.get());
		if (cropActivityItem.isPresent())
			expenceItem.setCropActivityItem(cropActivityItem.get());
		/*
		 * validation.valid(expenceItem); if(Validation.hasErrors()) {
		 * flash.error("Customer "+expenceItem+" could not be saved!Error="+Validation.
		 * errors().toString()); Logger.info("Error!!  "+
		 * Validation.errors().toString()); ////render("@createExpenceItem",
		 * expenceItem); }
		 */
		// System.out.println("cropActivity:::"+cropActivity);
		// System.out.println("expenceItem:::"+expenceItem);
		if (expenceItem != null)
			cropAccountingService.saveExpenceItem(expenceItem);

		List<ExpenceItem> expenceItemList = cropAccountingService.getExpenceItemList();
		// System.out.println("\n\n"+expenceItemList.toString());
		String json = expenceItemList.toString();
		json = json.substring(1, json.length() - 1);
		System.out.println("\n\n" + expenceItem.toStringJson());
		// renderText(expenceItem.toStringJson());
		// //renderJson(Json.toJson(expenceItemList));
		// //renderText(expenceItemList.toString());
		// flash.error("item saved");
		// //render("@expenceItemList",expenceItemList);
	}

	@RequestMapping("/cropmanagement/editExpenceItem")
	public String editExpenceItem(Long id, Model model) {
		Optional<ExpenceItem> expenceItem = cropAccountingService.getExpenceItem(id);
		List<CropActivity> cropActivityList = cropAccountingService.getCropActivityList();
		List<CropActivityType> cropActivityTypeList = cropAccountingService.getCropActivityTypeList();
		model.addAttribute("expenceItem", expenceItem);
		model.addAttribute("cropActivityList", cropActivityList);
		model.addAttribute("cropActivityTypeList", cropActivityTypeList);
		// render("@createExpenceItem", expenceItem, cropActivityList,
		// cropActivityTypeList);
		return "redirect:/cropmanagement/createExpenceItem";
	}

	@RequestMapping("/cropmanagement/expenceItemList")
	public void expenceItemList(Model model) {
		List<ExpenceItem> expenceItemList = cropAccountingService.findOrderedList();
		List<CropActivity> cropActivityList = cropAccountingService.getCropActivityList();
		List<CropActivityType> cropActivityTypeList = cropAccountingService.getCropActivityTypeList();
		List<CropActivityItem> cropActivityItemList = cropAccountingService.getCropActivityItemList();
		// render(expenceItemList, cropActivityList, cropActivityTypeList,
		// cropActivityItemList);
		model.addAttribute("expenceItemList", expenceItemList);
		model.addAttribute("cropActivityList", cropActivityList);
		model.addAttribute("cropActivityTypeList", cropActivityTypeList);
		model.addAttribute("cropActivityItemList", cropActivityItemList);
	}

	@RequestMapping("/cropmanagement/incomeItemList")
	public void incomeItemList(Model model) {
		List<IncomeItem> incomeItemList = cropAccountingService.getIncomeItemList();// IncomeItem.find("order by id
																					// desc").fetch();
		List<CropIncome> cropIncomeList = cropAccountingService.getCropIncomeList();
		List<CropIncomeItem> cropIncomeItemList = cropAccountingService.getCropIncomeItemList();
		// render(incomeItemList, cropIncomeList, cropIncomeItemList);
		model.addAttribute("incomeItemList", incomeItemList);
		model.addAttribute("cropIncomeList", cropIncomeList);
		model.addAttribute("cropIncomeItemList", cropIncomeItemList);
	}

	@RequestMapping("/cropmanagement/submit")
	public String submit(@ModelAttribute Crop crop, @RequestParam("type") String type, @RequestParam("crop") String cropIdStr,
			@RequestParam("varity") String varity, @RequestParam("crop.startDate") String date, Model model) {

		//javax.persistence.EntityManager em = play.db.jpa.JPA.em("other");
		/*List<Object[]> types = em.createNativeQuery("SELECT distinct a.type FROM crops a").getResultList();
		List<Object[]> crops = em.createNativeQuery("SELECT  a.type,a.name,a.id FROM crops a").getResultList();
		String query = "SELECT a.crop_id,a.name, a.local_name, a.spec,a.production_with_irrigation,a.production_without_irrigation,"
				+ "a.life_time, a.special FROM varieties a where crop_id is not null and deleted_at is null";
		// System.out.println("query::::"+query);
		List<Object[]> varities = em.createNativeQuery(query).getResultList();*/

		crop.setName("asdf");
		long cropId = 0L;
		long varityId = 0L;
		if (!type.isEmpty())
			type = type.replace("string:", "");
		if (!cropIdStr.isEmpty()) {
			cropIdStr = cropIdStr.replace("string:", "");
			cropId = Long.parseLong(cropIdStr);
		}
		if (!varity.isEmpty()) {
			varity = varity.replace("string:", "");
			varityId = Long.parseLong(varity);
		}
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		// final LocalDate dt = dtf.parseLocalDate(yourinput);
		LocalDate startDate = LocalDate.parse(date, dtf);
		crop.setStartDate(startDate);
		System.out.println("crop:::" + crop);
		FarmerCropTask farmerCropTask = new FarmerCropTask();
		farmerCropTask.setStartDate(startDate);

		Optional<CropExpenceList> cropExpenceList = cropAccountingService.getCropExpenceListByType(type, cropId,
				varityId);
		farmerCropTask.setCrop(cropId);
		farmerCropTask.setVarity(varityId);
		crop.setCrop(cropId);
		crop.setVarity(varityId);
		crop.setType(type);
		Optional<Crops> portalCrop = cropAccountingService.getCropsById(cropId);
		Optional<Varieties> portalCropVariety = cropAccountingService.getVarietiesById(varityId);
		if (portalCrop.isPresent())
			farmerCropTask.setCropName(portalCrop.get().getName());
		if (portalCropVariety.isPresent())
			farmerCropTask.setVarityName(portalCropVariety.get().getName());

		farmerCropTask.setType(type);
		List<FarmerTask> farmerTaskList = new ArrayList<>();
		List<ExpenceItem> expenceItemList = cropAccountingService.getExpenceItemList();
		// cropExpenceList.expenceItemValueList.stream().forEach(ex ->
		// System.out.println("ex:::"+ex));
		if (cropExpenceList.isPresent()) {
			farmerTaskList = cropExpenceList.get().getExpenceItemValueList().stream()
					.map(ex -> new FarmerTask(ex.getCropActivity(expenceItemList),
							ex.getCropActivityType(expenceItemList), ex.getCropActivityItem(), ex.getItemExpence(),
							ex.getLabourExpence(), ex.getCropCalenderTask().getTaskName(),
							ex.getCropCalenderTask().getTaskDateStr(), ex.getCropCalenderTask().getDateOfUpload(),
							java.time.LocalDateTime.of(
									startDate.plusDays(Long.parseLong(ex.getCropCalenderTask().getTaskDateStr())),
									java.time.LocalTime.NOON),
							java.time.LocalDateTime.of(
									startDate.plusDays(Long.parseLong(ex.getCropCalenderTask().getTaskDateStr())),
									java.time.LocalTime.MAX)))
					.collect(Collectors.toList());
		}

		farmerCropTask.setFarmerTaskList(farmerTaskList);
		System.out.println("farmerTaskList:::" + farmerTaskList.toString());
		System.out.println("farmerCropTask:::" + farmerCropTask);

		/*
		 * validation.valid(crop);
		 * 
		 * if (Validation.hasErrors()) { flash.error("Customer " + crop.farmer.name +
		 * " could not be saved!Error=" + Validation.errors().toString());
		 * Logger.info("Error!!  " + Validation.errors().toString()); // expenceItemList
		 * = ExpenceItem.find("order by id desc").fetch();
		 * 
		 * // List<CropActivity> cropActivityList = CropActivity.findAll(); //
		 * List<CropActivityType> cropActivityTypeList = // CropActivityType.findAll();
		 * // List<CropActivityItem> cropActivityItemList = //
		 * CropActivityItem.findAll(); //render("@croplist", crop, types, crops,
		 * varities); }
		 */

		cropAccountingService.saveCrop(crop);
		farmerCropTask.setFarmer(crop.getFarmer());
		cropAccountingService.saveFarmerCropTask(farmerCropTask);
		// //render("@farmerTaskExpenditure",crop,cropExpenceList,expenceItemList);
		System.out.println("cropExpenceList:::::::::::::::::::");
		System.out.println(cropExpenceList);
		// render("@cropprint", crop, cropExpenceList, expenceItemList);
		model.addAttribute("crop", crop);
		model.addAttribute("cropExpenceList", cropExpenceList);
		model.addAttribute("expenceItemList", expenceItemList);
		return "redirect:/cropmanagement/cropprint";
	}

	@PostMapping("/cropmanagement/submitExpenditure")
	public String submitExpenditure(@ModelAttribute CropExpenceList cropExpenceList,
			@RequestParam("cropExpenceList.expenceItemValue.cropActivityItem") String[] cropActivityItems,
			@RequestParam("cropExpenceList.expenceItemValue.itemExpence") String[] itemExpences,
			@RequestParam("cropExpenceList.expenceItemValue.labourExpence") String[] labourExpences,
			@RequestParam("taskId") String[] taskIds, Model model) {

		Optional<CropTaskMap> cropTaskMap = cropAccountingService
				.getCropTaskMap(cropExpenceList.getCropTaskMap().getId());
		Optional<CropExpenceList> repoCropExpenceList = cropAccountingService.getCropExpenceListByType(
				cropTaskMap.get().getType(), cropTaskMap.get().getCrop(), cropTaskMap.get().getVarity());

		// validation.valid(cropExpenceList);
		ExpenceItemValue expenceItemValue = null;
		Optional<CropActivityItem> cropActivityItem = null;
		Optional<CropCalenderTask> cropCalenderTask = null;
		List<ExpenceItemValue> expenceItemValueList = new ArrayList<>();
		float itemExp = 0;
		float labourExp = 0;
		for (int i = 0; cropActivityItems != null && i < cropActivityItems.length; i++) {
			expenceItemValue = new ExpenceItemValue();
			if (cropActivityItems[i] != null && cropActivityItems[i].length() > 0)
				cropActivityItem = cropAccountingService.getCropActivityItem(Long.parseLong(cropActivityItems[i]));
			if (cropActivityItem.isPresent())
				expenceItemValue.setCropActivityItem(cropActivityItem.get());

			if (taskIds[i] != null && taskIds[i].length() > 0)
				cropCalenderTask = cropAccountingService.getCropCalenderTaskById(Long.parseLong(taskIds[i]));

			if (cropCalenderTask.isPresent())
				expenceItemValue.setCropCalenderTask(cropCalenderTask.get());

			if (itemExpences[i] != null && itemExpences[i].length() > 0)
				itemExp = Float.parseFloat(itemExpences[i]);
			expenceItemValue.setItemExpence(itemExp);
			if (labourExpences[i] != null && labourExpences[i].length() > 0)
				labourExp = Float.parseFloat(labourExpences[i]);
			expenceItemValue.setLabourExpence(labourExp);

			expenceItemValueList.add(expenceItemValue);
		}
		if (repoCropExpenceList.isPresent())
			cropExpenceList = repoCropExpenceList.get();
		cropExpenceList.setExpenceItemValueList(expenceItemValueList);

		/*
		 * if (Validation.hasErrors()) { flash.error("Customer " +
		 * cropExpenceList.cropTaskMap.id + " could not be saved!Error=" +
		 * Validation.errors().toString()); Logger.info("Error!!  " +
		 * Validation.errors().toString());
		 * 
		 * // List<CropActivity> cropActivityList = CropActivity.findAll(); //
		 * List<CropActivityType> cropActivityTypeList = // CropActivityType.findAll();
		 * List<ExpenceItem> expenceItemList = ExpenceItem.findAll();
		 * //render("@createTaskExpenditure", cropTaskMap, expenceItemList,
		 * cropExpenceList); }
		 */
		if (cropTaskMap.isPresent()) {
			cropExpenceList.setType(cropTaskMap.get().getType());
			cropExpenceList.setCrop(cropTaskMap.get().getCrop());
			cropExpenceList.setVarity(cropTaskMap.get().getVarity());
			cropAccountingService.saveCropExpenceList(cropExpenceList);
		}
		List<CropExpenceList> cropExpences = cropAccountingService.getCropExpenceLists();
		// render("@cropexpencelist", cropExpences);
		model.addAttribute("cropExpences", cropExpences);
		return "redirect:/cropmanagement/cropexpencelist";
	}

	@RequestMapping("/cropmanagement/farmerTaskExpenditure")
	public void farmerTaskExpenditure(Long id, Model model) {
		System.out.println("id:::" + id);
		Optional<CropExpenceList> cropExpenceList = cropAccountingService.getExpenceListById(id);
		// Optional<CropTaskMap> cropTaskMap = cropAccountingService.getCropTaskMap(id);
		// List<CropActivity> cropActivityList = CropActivity.findAll();
		// List<CropActivityType> cropActivityTypeList =
		// CropActivityType.findAll();
		List<ExpenceItem> expenceItemList = cropAccountingService.getExpenceItemList();

		model.addAttribute("cropExpenceList", cropExpenceList);
		model.addAttribute("expenceItemList", expenceItemList);
		// render(cropExpenceList, expenceItemList);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/cropmanagement/otherdb")
	public void otherdb(Model model) {
		// Connection conn = play.db.DB.getDBConfig("other").getConnection();
		//javax.persistence.EntityManager em = play.db.jpa.JPA.em("other");
		// Query q
		List<Object[]> crops = em.createNativeQuery("SELECT a.name, a.type FROM crops a").getResultList();
		List<Object[]> types = em.createNativeQuery("SELECT distinct a.type FROM crops a").getResultList();
		String query = "SELECT a.crop_id,a.name, a.local_name, a.spec,a.production_with_irrigation,a.production_without_irrigation,"
				+ "a.life_time, a.special FROM varieties a where crop_id is not null and deleted_at is null";
		query = "SELECT a.crop_id,a.name FROM varieties a where crop_id is not null";
		System.out.println("query::::" + query);
		List<Object[]> varities = em.createNativeQuery(query).getResultList();

		for (Object a : types) {
			System.out.println("variety " + a);
		}
		model.addAttribute("crops", crops);
		model.addAttribute("varities", varities);
		// render(crops, varities);
	}

	@RequestMapping("/cropmanagement/print")
	public void print() {
		// render();
	}

	@RequestMapping("/cropmanagement/cropprint")
	public void cropprint(Model model) {
		Optional<Crop> crop = cropAccountingService.getCropById(618l);
		List<ExpenceItem> expenceItemList = cropAccountingService.getExpenceItemList();
		Optional<CropExpenceList> cropExpenceList = cropAccountingService.getCropExpenceListByType("field_crop", 27l,
				27l);
		System.out.println(crop);
		System.out.println(cropExpenceList);
		model.addAttribute("crop", crop.get());
		model.addAttribute("expenceItemList", expenceItemList);
		model.addAttribute("cropExpenceList", cropExpenceList);
		// render(crop, expenceItemList, cropExpenceList);
	}

	@RequestMapping("/cropmanagement/tbprint")
	public void tbprint() {
		// render();
	}
}
