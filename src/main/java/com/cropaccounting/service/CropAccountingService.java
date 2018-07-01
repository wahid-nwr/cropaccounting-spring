package com.cropaccounting.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.cropaccounting.models.FarmerCropTask;
import com.cropaccounting.models.FarmerTask;
import com.cropaccounting.models.IncomeItem;
import com.cropaccounting.models.UserModel;
import com.cropaccounting.models.Varieties;
import com.cropaccounting.repository.CropActivityItemRepository;
import com.cropaccounting.repository.CropActivityRepository;
import com.cropaccounting.repository.CropActivityTypeRepository;
import com.cropaccounting.repository.CropCalenderTaskRepository;
import com.cropaccounting.repository.CropExpenceListRepository;
import com.cropaccounting.repository.CropIncomeItemRepository;
import com.cropaccounting.repository.CropIncomeListRepository;
import com.cropaccounting.repository.CropIncomeRepository;
import com.cropaccounting.repository.CropRepository;
import com.cropaccounting.repository.CropTaskMapRepository;
import com.cropaccounting.repository.CropsRepository;
import com.cropaccounting.repository.ExpenceItemRepository;
import com.cropaccounting.repository.FarmerCropTaskRepository;
import com.cropaccounting.repository.FarmerTaskRepository;
import com.cropaccounting.repository.IncomeItemRepository;
import com.cropaccounting.repository.UserModelRepository;
import com.cropaccounting.repository.VarietiesRepository;

@Service
public class CropAccountingService {

	@Autowired
	private UserModelRepository userModelRepo;
	
	@Autowired
	private FarmerTaskRepository farmerTaskRepository;
	
	@Autowired
	private CropRepository cropRepository;

	@Autowired
	private CropsRepository cropsRepository;
	
	@Autowired
	private VarietiesRepository varietiesRepository;
	
	@Autowired
	private CropIncomeListRepository cropIncomeListRepository;
	
	@Autowired
	private CropIncomeRepository cropIncomeRepository;
	
	@Autowired
	private CropIncomeItemRepository cropIncomeItemRepository;

	@Autowired
	private CropActivityTypeRepository cropActivityTypeRepository;
	
	@Autowired
	private CropActivityItemRepository cropActivityItemRepository;
	
	@Autowired
	private CropActivityRepository cropActivityRepository;
	
	@Autowired
	private ExpenceItemRepository expenceItemRepository;
	
	@Autowired
	private CropTaskMapRepository cropTaskMapRepository;
	
	@Autowired
	private CropExpenceListRepository cropExpenceListRepository;
	
	@Autowired
	private IncomeItemRepository incomeItemRepository;
	
	@Autowired
	private CropCalenderTaskRepository cropCalenderTaskRepository;
	
	@Autowired
	private FarmerCropTaskRepository farmerCropTaskRepository;
	
	public FarmerTask saveFarmerTask(FarmerTask farmerTask) {
		return farmerTaskRepository.save(farmerTask);
	}
	
	public Crops saveCrops(Crops crops) {
		return cropsRepository.save(crops);
	}
	
	public Varieties saveVarieties(Varieties varieties) {
		return varietiesRepository.save(varieties);
	}
	
	public CropIncomeList saveCropIncomeList(CropIncomeList cropIncomeList) {
		return cropIncomeListRepository.save(cropIncomeList);
	}
	
	public CropIncome saveCropIncome(CropIncome cropIncome) {
		return cropIncomeRepository.save(cropIncome);
	}
	
	public CropIncomeItem saveCropIncomeItem(CropIncomeItem cropIncomeItem) {
		return cropIncomeItemRepository.save(cropIncomeItem);
	}
	
	public CropActivity saveCropActivity(CropActivity cropActivity) {
		return cropActivityRepository.save(cropActivity);
	}
	
	public CropActivityType saveCropActivityType(CropActivityType cropActivityType) {
		return cropActivityTypeRepository.save(cropActivityType);
	}
	
	public CropActivityItem saveCropActivityItem(CropActivityItem cropActivityItem) {
		return cropActivityItemRepository.save(cropActivityItem);
	}
	
	public CropTaskMap saveCropTaskMap(CropTaskMap cropTaskMap) {
		return cropTaskMapRepository.save(cropTaskMap);
	}
	
	public ExpenceItem saveExpenceItem(ExpenceItem expenceItem) {
		return expenceItemRepository.save(expenceItem);
	}
	
	public Crop saveCrop(Crop crop) {
		return cropRepository.save(crop);
	}
	
	public FarmerCropTask saveFarmerCropTask(FarmerCropTask farmerCropTask) {
		return farmerCropTaskRepository.save(farmerCropTask);
	}
	
	public CropExpenceList saveCropExpenceList(CropExpenceList cropExpenceList) {
		return cropExpenceListRepository.save(cropExpenceList);
	}
	
	public Optional<UserModel> getUsers(Long userId) {
		return userModelRepo.findById(userId);
	}
	
	public List<UserModel> getUsersExceptAdmin() {
		return userModelRepo.getUserExceptAdmin();
	}
	
	public Optional<FarmerTask> getFarmerTaskById(Long id) {
		return farmerTaskRepository.findById(id);
	}
	
	public List<FarmerCropTask> getFarmerCropTaskById(String nid) {
		return farmerCropTaskRepository.findByNID(nid);
	}
	
	public List<Crop> getCropList() {
		List<Crop> elementList = new ArrayList<>(); 
		cropRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public Optional<Crop> getCropById(Long id) {
		return cropRepository.findById(id);
	}
	
	public List<Crop> getCropByNID(String nid, LocalDate startDate) {
		return cropRepository.find(nid, startDate);
	}
	
	public List<Crops> getCropsList() {
		List<Crops> elementList = new ArrayList<>(); 
		cropsRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public Optional<Crops> getCropsById(Long id) {
		return cropsRepository.findById(id);
	}
	
	public Optional<Varieties> getVarietiesById(Long id) {
		return varietiesRepository.findById(id);
	}
	
	public Optional<CropIncomeList> getCropIncomeList(String type, long crop, long varity) {
		return cropIncomeListRepository.findOne(type, crop, varity);
	}
	
	public List<CropIncomeList> getCropIncomeLists(String type, long crop, long varity) {
		return cropIncomeListRepository.find(type, crop, varity);
	}
	
	public List<CropIncomeList> getCropIncomeLists() {
		List<CropIncomeList> elementList = new ArrayList<>(); 
		cropIncomeListRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public List<CropIncome> getCropIncomeList() {
		List<CropIncome> elementList = new ArrayList<>(); 
		cropIncomeRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public Optional<CropIncome> getCropIncomeById(Long id) {
		return cropIncomeRepository.findById(id);
	}
	
	public List<CropIncomeItem> getCropIncomeItemList() {
		List<CropIncomeItem> elementList = new ArrayList<>(); 
		cropIncomeItemRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public List<CropActivityType> getCropActivityTypeList() {
		List<CropActivityType> elementList = new ArrayList<>(); 
		cropActivityTypeRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public List<CropActivityItem> getCropActivityItemList() {
		List<CropActivityItem> elementList = new ArrayList<>(); 
		cropActivityItemRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public List<CropActivity> getCropActivityList() {
		List<CropActivity> elementList = new ArrayList<>(); 
		cropActivityRepository.findAll().forEach(elementList::add);
		return elementList;
	}

	public List<CropActivityType> getCropActivityTypelist() {
		List<CropActivityType> elementList = new ArrayList<>(); 
		cropActivityTypeRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public Optional<CropActivity> getCropActivity(Long id) {
		return cropActivityRepository.findById(id);
	}
	
	public Optional<CropActivityType> getCropActivityType(Long id) {
		return cropActivityTypeRepository.findById(id);
	}
	
	public Optional<CropActivityItem> getCropActivityItem(Long id) {
		return cropActivityItemRepository.findById(id);
	}
	
	public List<ExpenceItem> getExpenceItemList() {
		List<ExpenceItem> elementList = new ArrayList<>(); 
		expenceItemRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public List<ExpenceItem> findOrderedList() {
		List<ExpenceItem> elementList = new ArrayList<>(); 
		expenceItemRepository.findOrderedList().forEach(elementList::add);
		return elementList;
	}

	public List<ExpenceItem> getExpenceItemOrderedList() {
		List<ExpenceItem> elementList = new ArrayList<>(); 
		expenceItemRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public Optional<ExpenceItem> getExpenceItem(Long id) {
		return expenceItemRepository.findById(id);
	}
	
	public List<CropTaskMap> getCropTaskMapList() {
		List<CropTaskMap> elementList = new ArrayList<>(); 
		cropTaskMapRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public Optional<CropTaskMap> getCropTaskMap(Long id) {
		return cropTaskMapRepository.findById(id);
	}
	
	public List<CropExpenceList> getCropExpenceLists() {
		List<CropExpenceList> elementList = new ArrayList<>(); 
		cropExpenceListRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public List<IncomeItem> getIncomeItemList() {
		List<IncomeItem> elementList = new ArrayList<>(); 
		incomeItemRepository.findAll().forEach(elementList::add);
		return elementList;
	}
	
	public Optional<CropExpenceList> getCropExpenceListByType(String type, long crop, long varity) {
		return cropExpenceListRepository.find(type, crop, varity);
	}
	
	public Optional<CropExpenceList> getExpenceListById(Long id) {
		return cropExpenceListRepository.findById(id);
	}
	
	public Optional<CropCalenderTask> getCropCalenderTaskById(Long id) {
		return cropCalenderTaskRepository.findById(id);
	}
}
