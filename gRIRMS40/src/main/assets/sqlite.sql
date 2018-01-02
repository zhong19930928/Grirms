-- 删除表
DROP TABLE IF EXISTS MAIN_MENU;
DROP TABLE IF EXISTS VISIT_WAY;
DROP TABLE IF EXISTS VISIT_STORE;
DROP TABLE IF EXISTS VISIT_FUNC;
DROP TABLE IF EXISTS FUNC;
DROP TABLE IF EXISTS SUBMIT;
DROP TABLE IF EXISTS SUBMIT_ITEM;
DROP TABLE IF EXISTS SUBMITITEM_TEMP;
DROP TABLE IF EXISTS NOTICE;
DROP TABLE IF EXISTS TASK;
DROP TABLE IF EXISTS PUSH;
DROP TABLE IF EXISTS PUSH_ITEM;
DROP TABLE IF EXISTS ORG;
DROP TABLE IF EXISTS ORG_STORE;
DROP TABLE IF EXISTS ORG_USER;
DROP TABLE IF EXISTS MODULE;
DROP TABLE IF EXISTS FUNC_CONTROL;
DROP TABLE IF EXISTS ROLE;
DROP TABLE IF EXISTS QA;
DROP TABLE IF EXISTS DOWNLOAD_INFO;
DROP TABLE IF EXISTS PSS_CONF;
DROP TABLE IF EXISTS PSS_PRODUCT_CONF;
DROP TABLE IF EXISTS ORDER_CACHE;
DROP TABLE IF EXISTS ORDER_SALE;
--DROP TABLE IF EXISTS TABLE_STENCIL;
DROP TABLE IF EXISTS QUERY;
DROP TABLE IF EXISTS ORDER_CONTACTS;
DROP TABLE IF EXISTS NEW_ORDER;
DROP TABLE IF EXISTS STYLE;
DROP TABLE IF EXISTS ORDER3_PRODUCT_CONF;
DROP TABLE IF EXISTS ORDER3_PROMOTION;
DROP TABLE IF EXISTS ORDER3_SHOPPING_CART;
DROP TABLE IF EXISTS ORDER3_CONTACTS;
DROP TABLE IF EXISTS ORDER3_PRODUCT_CTRL;
DROP TABLE IF EXISTS ORDER3;
DROP TABLE IF EXISTS ORDER3_PRODUCT_DATA;
DROP TABLE IF EXISTS ORDER3_DIS;

DROP TABLE IF EXISTS CAR_SALES_PRODUCT_CONF;
DROP TABLE IF EXISTS CAR_SALES_PROMOTION;
DROP TABLE IF EXISTS CAR_SALES_SHOPPING_CART;
DROP TABLE IF EXISTS CAR_SALES_CONTACTS;
DROP TABLE IF EXISTS CAR_SALES_PRODUCT_CTRL;
DROP TABLE IF EXISTS CAR_SALES;
DROP TABLE IF EXISTS CAR_SALES_PRODUCT_DATA;
DROP TABLE IF EXISTS CAR_SALES_STOCK;

DROP TABLE IF EXISTS ATTENTION;
DROP TABLE IF EXISTS COMMENT;
DROP TABLE IF EXISTS GROUP_TABLE;
DROP TABLE IF EXISTS GROUP_ROLE;
DROP TABLE IF EXISTS TOPIC;
DROP TABLE IF EXISTS TOPIC_NOTIFY;
DROP TABLE IF EXISTS ZAN;
DROP TABLE IF EXISTS REPLY;
DROP TABLE IF EXISTS NOTIFICATION;
DROP TABLE IF EXISTS SURVEY;
DROP TABLE IF EXISTS PERSONAL_WECHAT;


DROP TABLE IF EXISTS ADRESS_BOOK_USER;

DROP TABLE IF EXISTS ANSWER_OPTIONS;
DROP TABLE IF EXISTS FINDING_DETAIL;
DROP TABLE IF EXISTS FINDINGS;
DROP TABLE IF EXISTS QUESTION;
DROP TABLE IF EXISTS QUESTIONNAIRE;
DROP TABLE IF EXISTS SURVEY_ADRESS;

DROP TABLE IF EXISTS EAXM_ANSWER_OPTIONS;
DROP TABLE IF EXISTS EXAMINATION;
DROP TABLE IF EXISTS EXAM_QUESTION;
DROP TABLE IF EXISTS EXAM_RESULT;
DROP TABLE IF EXISTS EXAM_RESULT_DETAIL;
DROP TABLE IF EXISTS DOUBLE_TASK_MANAGER;
DROP TABLE IF EXISTS IS_LEAVE;
DROP TABLE IF EXISTS AF_LEAVE_INFO;
DROP TABLE IF EXISTS FUNC_CACHE;
DROP TABLE IF EXISTS WORK_PLAN_MODLE;
DROP TABLE IF EXISTS PLAN_DATA;
DROP TABLE IF EXISTS PLAN_ASSESS;
DROP TABLE IF EXISTS WORK_SUMMARY_DATA;
DROP TABLE IF EXISTS ADV_PICTURE;

DROP TABLE IF EXISTS MAIN_SHIHUA_MENU;
DROP TABLE IF EXISTS MEETING_USER_CTRL;


-- Home页模块
CREATE TABLE IF NOT EXISTS MAIN_MENU (
	id INTEGER primary key autoincrement not null,
	menu_id INTEGER,
	name VARCHAR,
	type INTEGER,
	module_type INTEGER,
	base_time VARCHAR,
	submitTime VARCHAR,
	phoneSerialNo INTEGER,
	is_no_wait INTEGER,
	phoneUsableTime VARCHAR
);
--插入管理模块
insert into MAIN_MENU (menu_id,name,type,phoneSerialNo)values(-1,'未提交数据管理',15,99999);


-- 访店线路
CREATE TABLE IF NOT EXISTS VISIT_WAY (
	id INTEGER primary key autoincrement not null,
	way_id INTEGER,
	name VARCHAR,
	is_order INTEGER,
	plan_id INTEGER,
	awokeType INTEGER,
	intervalType INTEGER,
	weekly VARCHAR,
	fromDate VARCHAR,
	toDate VARCHAR,
	startdate VARCHAR,
	cycle_count INTEGER,
	visit_count INTEGER

);
-- 访店店面
CREATE TABLE IF NOT EXISTS VISIT_STORE (
	id INTEGER primary key autoincrement not null,
	store_id INTEGER,
	name VARCHAR,
	way_id INTEGER,
	targetid INTEGER,
	isCheckin INTEGER,
	isCheckout INTEGER,
	submitDate VARCHAR,
	planId INTEGER,
	deleteDate VARCHAR,
	lon VARCHAR,
	lat VARCHAR,
	isCheck INTEGER,
	storeDistance INTEGER,
	loc_type VACHAR,
	is_address VACHAR,
	is_anew_loc VACHAR,
	is_no_wait INTEGER,
	submit_num INTEGER

);
-- 访店模块控件
CREATE TABLE IF NOT EXISTS VISIT_FUNC (
	id INTEGER primary key autoincrement not null,
	func_id INTEGER,
	targetid INTEGER,
	menuid INTEGER,
	name VARCHAR,
	type INTEGER,
	length INTEGER,
	isEmpty INTEGER,
	checkType INTEGER,
	nextDropdown VARCHAR,
	defaultType INTEGER,
	defaultValue VARCHAR,
	dict_table VARCHAR,
	dict_data_id VARCHAR,
	dict_cols VARCHAR,
	parentid VARCHAR,
	dataType INTEGER,
	orgOption INTEGER,
	orgLevel INTEGER,
	webOrder INTEGER,
	isShowColumn INTEGER,
	isSearch INTEGER, 
	status INTEGER,
	replenish_able_status VARCHAR,
	dupallowdays INTEGER,
	dupallowtimes INTEGER,
	tableId INTEGER,
	is_search_modify INTEGER,
	input_order INTEGER,
	enter_must_list VARCHAR,
	isFuzzy INTEGER,
	isSearchMul INTEGER,
	is_edit INTEGER,
	is_import_key INTEGER,
	colWidth INTEGER,
	photoTimeType INTEGER,
	codeType VARCHAR,
	dict_order_by VARCHAR,
	dict_is_asc VARCHAR,
	is_area_search INTEGER,
	area_search_value FLOAT,
	code_control VARCHAR,
	code_update VARCHAR,
	photoLocationType INTEGER,
	loc_type VACHAR,
	is_address VACHAR,
	is_anew_loc VACHAR,
	locCheckDistance VACHAR,
	task_status VARCHAR,
	print_alignment INTEGER,
	photo_flg INTEGER,
	is_img_custom INTEGER,
	is_img_store INTEGER,
	is_img_user INTEGER,
	ableAD VARCHAR,
	showColor VARCHAR,
	locLevel VARCHAR,
	is_cache INTEGER,
	restrict_type INTEGER,
	restrict_rule VARCHAR
);
-- 自定义模块控件
CREATE TABLE IF NOT EXISTS FUNC (
	id INTEGER primary key autoincrement not null,
	func_id INTEGER,
	targetid INTEGER,
	menuid INTEGER,
	name VARCHAR,
	type INTEGER,
	length INTEGER,
	isEmpty INTEGER,
	checkType INTEGER,
	nextDropdown VARCHAR,
	defaultType INTEGER,
	defaultValue VARCHAR,
	dict_table VARCHAR,
	dict_data_id VARCHAR,
	dict_cols VARCHAR,
	parentid VARCHAR,
	dataType INTEGER,
	orgOption INTEGER,
	orgLevel INTEGER,
	webOrder INTEGER,
	isShowColumn INTEGER,
	isSearch INTEGER, 
	status INTEGER,
	replenish_able_status VARCHAR,
	dupallowdays INTEGER,
	dupallowtimes INTEGER,
	tableId INTEGER,
	is_search_modify INTEGER,
	input_order INTEGER,
	enter_must_list VARCHAR,
	isFuzzy INTEGER,
	isSearchMul INTEGER,
	is_edit INTEGER,
	is_import_key INTEGER,
	colWidth INTEGER,
	photoTimeType INTEGER,
	codeType VARCHAR,
	dict_order_by VARCHAR,
	dict_is_asc VARCHAR,
	is_area_search INTEGER,
	area_search_value FLOAT,
	code_control VARCHAR,
	code_update VARCHAR,
	photoLocationType INTEGER,
	loc_type VACHAR,
	is_address VACHAR,
	is_anew_loc VACHAR,
	locCheckDistance VACHAR,
	task_status VARCHAR,
	print_alignment INTEGER,
	photo_flg INTEGER,
	is_img_custom INTEGER,
	is_img_store INTEGER,
	is_img_user INTEGER,
	ableAD VARCHAR,
	showColor VARCHAR,
	locLevel VARCHAR,
	is_cache INTEGER,
	restrict_type INTEGER,
	restrict_rule VARCHAR,
	is_scan INTEGER,
	scan_status INTEGER,
	scan_cols VARCHAR
);
--插入考勤
insert into FUNC (func_id,targetid,name,type,is_edit)values(-1,-1,'定位',2,1);
insert into FUNC (func_id,targetid,name,type,is_edit,photoTimeType)values(-2,-1,'拍照',1,1,2);
insert into FUNC (func_id,targetid,name,type,length,dataType,is_edit)values(-3,-1,'说明',3,500,5,'1');
insert into FUNC (func_id,targetid,name,type,isSearch,is_edit)values(-5,-2,'用户',19,1,1);
insert into FUNC (func_id,targetid,name,type,isSearch,is_edit)values(-6,-2,'时间',11,1,1);

-- 数据提交
CREATE TABLE IF NOT EXISTS SUBMIT (
	id INTEGER primary key autoincrement not null,
	parentId INTEGER,
	state INTEGER,
	plan_id INTEGER,
	way_id INTEGER,
	way_name VARCHAR,
	store_id INTEGER,
	store_name VARCHAR,
	targetid INTEGER,
	timestamp VARCHAR,
	targetType INTEGER,
	checkinGis VARCHAR,
	checkinTime VARCHAR,
	checkoutTime VARCHAR,
	checkoutGis VARCHAR,
	doubleId INTEGER,
	modType INTEGER,
	sendUserId VARCHAR,
	upCtrlMain VARCHAR,
	upCtrlTable VARCHAR,
	codeUpdate VARCHAR,
	codeUpdateTab VARCHAR,
	storeVisitNumbers INTEGER,
	doubleMasterTaskNo VARCHAR,
	doubleBtnType VARCHAR,
	contentDescription VARCHAR,
	menuId INTEGER,
	menuType INTEGER,
	menuName VARCHAR,
	isCacheFun INTEGER
);
CREATE TABLE IF NOT EXISTS SUBMIT_ITEM (
	id INTEGER primary key autoincrement not null,
	submit_id INTEGER,
	param_name VARCHAR, 
	param_value VARCHAR,
	type INTEGER,
	orderId INTEGER,
	note VARCHAR,
	isCacheFun INTEGER
);
CREATE TABLE IF NOT EXISTS SUBMITITEM_TEMP (
	id INTEGER primary key autoincrement not null,
	submit_id INTEGER,
	param_name VARCHAR, 
	param_value VARCHAR,
	type INTEGER,
	orderId INTEGER,
	isSave INTEGER,
	note VARCHAR,
	isCacheFun INTEGER
);
--公告
CREATE TABLE IF NOT EXISTS NOTICE (
	id INTEGER primary key autoincrement not null,
	noticeTitle VARCHAR,
	detailNotice VARCHAR,
	createUser VARCHAR,
	createTime VARCHAR,
	isread INTEGER,
	notifyId INTEGER,
	createOrg VARCHAR,
	dataType VARCHER,
	attachment VARCHER
);
--自定义查询列表
CREATE TABLE IF NOT EXISTS TASK (
	id INTEGER primary key autoincrement not null,
	taskTitle VARCHAR, detailTask VARCHAR,createUser VARCHAR,
	createTime VARCHAR,
	isread INTEGER,
	moduleid INTEGER,
	taskId INTEGER
);
--推送信息
CREATE TABLE IF NOT EXISTS PUSH (
	id INTEGER primary key autoincrement not null,
	createDate VARCHAR,
	msgId VARCHAR,
	queueId VARCHAR,
	isFinish INTEGER
);
CREATE TABLE IF NOT EXISTS PUSH_ITEM (
	id INTEGER primary key autoincrement not null,
	type INTEGER,
	content VARCHAR,
	status VARCHAR,
	msgId VARCHAR
);

--组织机构
CREATE TABLE IF NOT EXISTS ORG (
	id INTEGER primary key autoincrement not null, 
	orgId Integer,
	orgName VARCHAR,
	orgLevel VARCHAR,
	parentId VARCHAR,
	code VARCHAR
);
CREATE TABLE IF NOT EXISTS ORG_STORE (
	id INTEGER primary key autoincrement not null,
	storeId INTEGER, 
	orgId INTEGER,
	storeName VARCHAR,
	storeLon DOUBLE,
	storeLat DOUBLE,
	orgCode VARCHAR,
	level INTEGER
);
CREATE TABLE IF NOT EXISTS ORG_USER (
	id INTEGER primary key autoincrement not null,
	userId INTEGER, 
	orgId INTEGER,
	userName VARCHAR,
	pid INTEGER,
	purview VARCHAR,
	authsearch INTEGER,
	authorgid VARCHAR,
	org_code VARCHAR,
	roleId INTEGER,
	roleName VARCHAR
);
--自定义模块
CREATE TABLE IF NOT EXISTS MODULE (
	id INTEGER primary key autoincrement not null,
	menuId INTEGER, 
	type INTEGER,
	name VARCHAR,
	auth INTEGER,
	auth_org_id VARCHAR,
	org_code VARCHAR,
	is_cancel INTEGER,
	phoneTaskFuns VARCHAR,
	dynamicStatus VARCHAR,
	is_report_task VARCHAR
);
--控件的控件条件
CREATE TABLE IF NOT EXISTS FUNC_CONTROL (
	id INTEGER primary key autoincrement not null,
	funcId INTEGER, 
	targetId INTEGER,
	value_ VARCHAR,
	updateDate VARCHAR,
	submitState Integer
);
--权限（未用）
CREATE TABLE IF NOT EXISTS ROLE (
	id INTEGER primary key autoincrement not null,
	roleId INTEGER,
	name VARCHAR,
	level VARCHAR
);
--帮助：问答
CREATE TABLE IF NOT EXISTS QA (
	id INTEGER primary key autoincrement not null,
	question VARCHAR,
	answer VARCHAR
);
--APK断点续传
CREATE TABLE IF NOT EXISTS DOWNLOAD_INFO (
	id INTEGER primary key autoincrement not null, 
	thread_id INTEGER,
	start_pos INTEGER, 
	end_pos INTEGER, 
	compelete_size INTEGER,
	url VARCHAR,
	md5 VARCHAR
);
--旧订单
CREATE TABLE IF NOT EXISTS PSS_CONF (
	id INTEGER primary key autoincrement not null, 
	phone_fun VARCHAR,
	create_order_start_time VARCHAR, 
	create_order_end_time VARCHAR,
	sales_start_time VARCHAR,
	sales_end_time VARCHAR,
	returned_start_time VARCHAR,
	returned_end_time VARCHAR,
	returned_reason_dict_table VARCHAR,
	returned_reason_dict_data_id VARCHAR,
	stocktake_differ_dict_table VARCHAR,
	stocktake_differ_dict_data_id VARCHAR,
	create_order_time_conf VARCHAR,
	create_order_time_weekly VARCHAR,
	sales_time_conf VARCHAR,
	sales_time_weekly VARCHAR,
	returned_time_conf VARCHAR,
	returned_time_weekly VARCHAR,
	is_price_edit VARCHAR,
	order_print_style VARCHAR,
	stock_print_style VARCHAR,
	dict_order_by VARCHAR,
	dict_is_asc VARCHAR
);
CREATE TABLE IF NOT EXISTS PSS_PRODUCT_CONF (
	id INTEGER primary key autoincrement not null, 
	dict_table VARCHAR,
	dict_data_id VARCHAR,
	dictCols VARCHAR,
	next VARCHAR,
	name VARCHAR,
	dict_order_by VARCHAR,
	dict_is_asc VARCHAR
);
CREATE TABLE IF NOT EXISTS ORDER_CACHE (
	id INTEGER primary key autoincrement not null,
	orderNumber VARCHAR,
	orderProductId INTEGER,
	orderProductName VARCHAR,
	unitPrice DOUBLE,
	totalPrice VARCHAR,
	availableOrderQuantity BIGINT,
	periodDate VARCHAR,
	totalSales BIGINT,
	projectedInventoryQuantity BIGINT,
	orderQuantity BIGINT
);
CREATE TABLE IF NOT EXISTS ORDER_SALE (
	id INTEGER primary key autoincrement not null,
	proid INTEGER,
	storeid VARCHAR,
	name VARCHAR,
	price DOUBLE,
	sales BIGINT
);
--表格模板
CREATE TABLE IF NOT EXISTS TABLE_STENCIL (
	id INTEGER primary key autoincrement not null,
	targetid INTEGER,
	funcid INTEGER,
	value VARCHAR
);
--查询条件缓存值
CREATE TABLE IF NOT EXISTS QUERY (
	id INTEGER primary key autoincrement not null,
	mid INTEGER,
	funcid VARCHAR,
	value VARCHAR
);
--新订单
CREATE TABLE IF NOT EXISTS ORDER_CONTACTS (
	id INTEGER primary key autoincrement not null,
	storeId VARCHAR,orderContactsId INTEGER,
	userAddress VARCHAR, 
	userName VARCHAR,
	userPhone1 VARCHAR, 
	userPhone2 VARCHAR, 
	userPhone3 VARCHAR
);
CREATE TABLE IF NOT EXISTS NEW_ORDER (
	id INTEGER primary key autoincrement not null,
	storeId VARCHAR,
	orderData VARCHAR
);

CREATE TABLE IF NOT EXISTS TABLE_PENDING (
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	type INTEGER,
	status INTEGER,
	title VARCHAR,
	content VARCHAR,
	createDate VARCHAR,
	numberOfTimes INTEGER,
	package TEXT
);

CREATE TABLE IF NOT EXISTS TODO (
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	m_name VARCHAR,
	s_name VARCHAR,
	todo_num INTEGER
);

CREATE TABLE IF NOT EXISTS LOCATION_PENDING (
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	value VARCHAR,
	type INTEGER
);

CREATE TABLE IF NOT EXISTS STYLE(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,	
	style_type INTEGER,
	module_id INTEGER,
	img_url VARCHAR,
	img_name VARCHAR,
	bg_color VARCHAR,
	img_md5 VARCHAR,
	type INTEGER
		
);

CREATE TABLE IF NOT EXISTS ORDER3_PRODUCT_CONF (
	id INTEGER primary key autoincrement not null, 
	dict_table VARCHAR,
	dict_data_id VARCHAR,
	dictCols VARCHAR,
	next VARCHAR,
	name VARCHAR,
	type INTEGER
);
	
CREATE TABLE IF NOT EXISTS ORDER3_PROMOTION (
	id INTEGER primary key autoincrement not null, 
	promotionId INTEGER,
	name VARCHAR,
	mCnt VARCHAR,
	sCnt VARCHAR,
	amount VARCHAR,
	preType INTEGER,
	disType INTEGER,
	isDouble INTEGER,
	fTime VARCHAR,
	tTime VARCHAR,
	disRate VARCHAR,
	disAmount VARCHAR,
	level VARCHAR,
	mark VARCHAR,
	orgId VARCHAR,
	mTab VARCHAR,
	mId VARCHAR,
	mUid VARCHAR,
	sTab VARCHAR,
	sId VARCHAR,
	sUid VARCHAR
);

CREATE TABLE IF NOT EXISTS ORDER3_SHOPPING_CART (
	id INTEGER primary key autoincrement not null, 
	productId INTEGER,
	pId VARCHAR,
	promotionIds VARCHAR,
	unitId INTEGER,
	number VARCHAR,
	subtotal VARCHAR,
	pitcOn INTEGER,
	disAmount VARCHAR,
	disNumber VARCHAR,
	discountPrice VARCHAR,
	giftId INTEGER,
	giftUnitId INTEGER,
	preAmount VARCHAR,
	prePrice VARCHAR,
	nowProductPrict VARCHAR
);

CREATE TABLE IF NOT EXISTS ORDER3_CONTACTS (
	id INTEGER primary key autoincrement not null,
	storeId VARCHAR,
	orderContactsId INTEGER,
	userAddress VARCHAR, 
	userName VARCHAR,
	userPhone1 VARCHAR, 
	userPhone2 VARCHAR, 
	userPhone3 VARCHAR
);

CREATE TABLE IF NOT EXISTS ORDER3_PRODUCT_CTRL (
	id INTEGER primary key autoincrement not null,
	cId VARCHAR,
	pId VARCHAR,
	label VARCHAR,
	productId INTEGER,
	unitId INTEGER,
	count INTEGER
);
CREATE TABLE IF NOT EXISTS ORDER3 (
	id INTEGER primary key autoincrement not null,
	orderId INTEGER,
	orderNo VARCHAR,
	storeId VARCHAR,
	storeName VARCHAR,
	contactId INTEGER,
	orderAmount VARCHAR,
	actualAmount VARCHAR,
	payAmount VARCHAR,
	orderDiscount VARCHAR,
	unPayAmount VARCHAR,
	note VARCHAR,
	isPromotion INTEGER,
	orderTime VARCHAR,
	orderState VARCHAR,
	isCommbine INTEGER
);
CREATE TABLE IF NOT EXISTS ORDER3_PRODUCT_DATA (
	id INTEGER primary key autoincrement not null,
	dataId INTEGER,
	productId INTEGER,
	orderCount VARCHAR,
	actualCount VARCHAR,
	productUnit VARCHAR,
	unitId INTEGER,
	orderPrice VARCHAR,
	orderAmount VARCHAR,
	actualAmount VARCHAR,
	status VARCHAR,
	isOrderMain INTEGER,
	promotionId INTEGER,
	mainProductId INTEGER,
	orderRemark VARCHAR,
	orderNo VARCHAR,
	sendCount VARCHAR,
	currentSendCount VARCHAR,
	productName VARCHAR
);

CREATE TABLE IF NOT EXISTS ORDER3_DIS (
	id INTEGER primary key autoincrement not null,
	disId INTEGER,
	productId INTEGER,
	orgId INTEGER,
	orgCode VARCHAR,
	orgLevel INTEGER
);

CREATE TABLE IF NOT EXISTS CAR_SALES_CONTACTS (
	id INTEGER primary key autoincrement not null,
	storeId VARCHAR,
	contactsId INTEGER,
	userAddress VARCHAR, 
	userName VARCHAR,
	userPhone1 VARCHAR, 
	userPhone2 VARCHAR, 
	userPhone3 VARCHAR
);

CREATE TABLE IF NOT EXISTS CAR_SALES_PRODUCT_CONF (
	id INTEGER primary key autoincrement not null, 
	dict_table VARCHAR,
	dict_data_id VARCHAR,
	dictCols VARCHAR,
	next VARCHAR,
	name VARCHAR,
	type INTEGER
);

CREATE TABLE IF NOT EXISTS CAR_SALES_PRODUCT_CTRL (
	id INTEGER primary key autoincrement not null,
	cId VARCHAR,
	pId VARCHAR,
	label VARCHAR,
	levelLable VARCHAR,
	productId INTEGER,
	unitId INTEGER,
	count INTEGER,
	returnCount VARCHAR,
	loadingCount VARCHAR,
	unLoadingCount VARCHAR,
	replenishmentCount VARCHAR,
	outCount VARCHAR,
	inverty VARCHAR,
	dataId INTEGER
);

CREATE TABLE IF NOT EXISTS CAR_SALES_PROMOTION (
	id INTEGER primary key autoincrement not null, 
	promotionId INTEGER,
	name VARCHAR,
	mCnt VARCHAR,
	sCnt VARCHAR,
	amount VARCHAR,
	preType INTEGER,
	disType INTEGER,
	isDouble INTEGER,
	fTime VARCHAR,
	tTime VARCHAR,
	disRate VARCHAR,
	disAmount VARCHAR,
	level VARCHAR,
	mark VARCHAR,
	orgId VARCHAR,
	mTab VARCHAR,
	mId VARCHAR,
	mUid VARCHAR,
	sTab VARCHAR,
	sId VARCHAR,
	sUid VARCHAR
);

CREATE TABLE IF NOT EXISTS CAR_SALES_SHOPPING_CART (
	id INTEGER primary key autoincrement not null, 
	productId INTEGER,
	pId VARCHAR,
	promotionIds VARCHAR,
	unitId INTEGER,
	number VARCHAR,
	subtotal VARCHAR,
	pitcOn INTEGER,
	disAmount VARCHAR,
	disNumber VARCHAR,
	discountPrice VARCHAR,
	giftId INTEGER,
	giftUnitId INTEGER,
	preAmount VARCHAR,
	prePrice VARCHAR,
	nowProductPrict VARCHAR
);

CREATE TABLE IF NOT EXISTS CAR_SALES (
	id INTEGER primary key autoincrement not null,
	carSalesId INTEGER,
	carSalesNo VARCHAR,
	storeId VARCHAR,
	storeName VARCHAR,
	contactId INTEGER,
	carSalesAmount VARCHAR,
	actualAmount VARCHAR,
	payAmount VARCHAR,
	carSalesDiscount VARCHAR,
	unPayAmount VARCHAR,
	note VARCHAR,
	isPromotion INTEGER,
	carSalesTime VARCHAR,
	carSalesState VARCHAR,
	isCommbine INTEGER,
	salesDate VARCHAR,
	carNo VARCHAR,
	salesUser VARCHAR,
	salesPhone VARCHAR,
	driverUser VARCHAR,
	driverPhone VARCHAR,
	status VARCHAR,
	carId VARCHAR,
	returnAmount VARCHAR
);

CREATE TABLE IF NOT EXISTS CAR_SALES_PRODUCT_DATA (
	id INTEGER primary key autoincrement not null,
	dataId INTEGER,
	productId INTEGER,
	carSalesCount VARCHAR,
	actualCount VARCHAR,
	productUnit VARCHAR,
	unitId INTEGER,
	carSalesPrice VARCHAR,
	carSalesAmount VARCHAR,
	actualAmount VARCHAR,
	status VARCHAR,
	isCarSalesMain INTEGER,
	promotionId INTEGER,
	mainProductId INTEGER,
	carSalesRemark VARCHAR,
	carSalesNo VARCHAR,
	sendCount VARCHAR,
	currentSendCount VARCHAR,
	productName VARCHAR,
	stockNum VARCHAR
);

CREATE TABLE IF NOT EXISTS CAR_SALES_STOCK (
	id INTEGER primary key autoincrement not null,
	productId INTEGER,
	unitId INTEGER,
	productName VARCHAR,
	stockNum VARCHAR,
	stockoutNum VARCHAR,
	replenishmentNum VARCHAR,
	unit VARCHAR
);

CREATE TABLE IF NOT EXISTS ATTENTION (
	id INTEGER primary key autoincrement not null,
	attentionId INTEGER,
	userId INTEGER,
	type INTEGER,
	topicId INTEGER,
	noticeId INTEGER,
	date VARCHAR
);

CREATE TABLE IF NOT EXISTS REPLY (
	id INTEGER primary key autoincrement not null,
	replyId INTEGER,
	topicId INTEGER,
	level INTEGER,
	userId INTEGER,
	survey VARCHAR,
	content VARCHAR,
	date VARCHAR,
	adress VARCHAR,
	attachment VARCHAR,
	photo VARCHAR,
	pathCode VARCHAR,
	isSend INTEGER,
	replyName VARCHAR,
	isRead INTEGER,
	headImg VARCHAR,
	topicType VARCHAR,
	msgKey VARCHAR,
	isPublic VARCHAR,
	isClose INTEGER,
	authUserId INTEGER,
	authUserName VARCHAR,
	delStatus VARCHAR
);
CREATE TABLE IF NOT EXISTS GROUP_TABLE (
	id INTEGER primary key autoincrement not null,
	groupId INTEGER,
	groupName VARCHAR,
	explain VARCHAR,
	groupUser VARCHAR,
	orgId VARCHAR,
	orgCode VARCHAR,
	logo VARCHAR,
	type INTEGER,
	groupRole VARCHAR,
	createUserId INTGER
);
CREATE TABLE IF NOT EXISTS GROUP_ROLE (
	id INTEGER primary key autoincrement not null,
	groupId INTEGER,
	roleId INTEGER,
	roleName VARCHAR
);
CREATE TABLE IF NOT EXISTS GROUP_USER (
	id INTEGER primary key autoincrement not null,
	groupId INTEGER,
	userId INTEGER,
	userName VARCHAR,
	photo VARCHAR
);
CREATE TABLE IF NOT EXISTS SURVEY (
	id INTEGER primary key autoincrement not null,
	surveyId INTEGER,
	topicId INTEGER,
	explain VARCHAR,
	title VARCHAR,
	type INTEGER,
	surveyType INTEGER,
	options VARCHAR,
	optionOrder INTEGER
);
CREATE TABLE IF NOT EXISTS TOPIC (
	id INTEGER primary key autoincrement not null,
	topicId INTEGER,
	groupId INTEGER,
	title VARCHAR,
	explain VARCHAR,
	type INTEGER,
	from_ VARCHAR,
	to_ VARCHAR,
	speakNum INTEGER,
	isReply INTEGER,
	comment INTEGER,
	replyReview INTEGER,
	createUserId INTEGER,
	createTime VARCHAR,
	classify INTEGER,
	recentTime VARCHAR,
	options VARCHAR,
	selectType VARCHAR,
	isAttention INTEGER,
	isClose INTEGER,
	recentContent VARCHAR,
	msgKey VARCHAR,
	createUserName VARCHAR,
	createOrgName VARCHAR
);

CREATE TABLE IF NOT EXISTS TOPIC_NOTIFY (
	id INTEGER primary key autoincrement not null,
	topicId INTEGER,
	content VARCHAR,
	date VARCHAR
);

CREATE TABLE IF NOT EXISTS ZAN (
	id INTEGER primary key autoincrement not null,
	zanId INTEGER,
	topicId INTEGER,
	replayId INTEGER,
	userId INTEGER,
	date VARCHAR,
	isSend INTEGER,
	userName VARCHAR
);

CREATE TABLE IF NOT EXISTS PERSONAL_WECHAT (
	id INTEGER primary key autoincrement not null,
	dataId INTEGER,
	sUserId INTEGER,
	sUserName VARCHAR,
	dUserId INTEGER,
	dUserName VARCHAR,
	cUserHeadImg VARCHAR,
	dUserHedaImg VARCHAR,
	attachment VARCHAR,
	content VARCHAR,
	date VARCHAR,
	msgKey VARCHAR,
	groupKey VARCHAR,
	photo VARCHAR,
	isRead INTEGER
);

CREATE TABLE IF NOT EXISTS COMMENT (
	id INTEGER primary key autoincrement not null,
	replyId INTEGER,
	commentId INTEGER,
	comment VARCHAR,
	cUserId INTEGER,
	cUserName VARCHAR,
	dUserId INTEGER,
	dUserName VARCHAR,
	pathCode VARCHAR,
	date VARCHAR,
	isSend INTEGER,
	topicId VARCHAR,
	msgKey VARCHAR,
	isPublic VARCHAR,
	authUserId INTEGER,
	authUserName VARCHAR
);

CREATE TABLE IF NOT EXISTS NOTIFICATION (
	id INTEGER primary key autoincrement not null,
	noticeId INTEGER,
	title VARCHAR,
	content VARCHAR,
	from_ VARCHAR,
	to_ VARCHAR,
	attachment VARCHAR,
	peoples VARCHAR,
	orgId INTEGER,
	orgCode VARCHAR,
	creater VARCHAR,
	createOrg VARCHAR,
	createDate VARCHAR,
	role VARCHAR,
	users VARCHAR,
	isAttach VARCHAR,
	isNoticed VARCHAR,
	isRead VARCHAR
);

CREATE TABLE IF NOT EXISTS ADRESS_BOOK_USER (
	id INTEGER primary key autoincrement not null,
	uId INTEGER,
	rId INTEGER,
	un VARCHAR,
	pn VARCHAR,
	rn VARCHAR,
	rl INTEGER,
	ons VARCHAR,
	oc VARCHAR,
	ol INTEGER,
	oId INTEGER,
	op VARCHAR,
	isVisit INTEGER,
	unClickble INTEGER,
	mailAddr VARCHAR,
	photo VARCHAR,
	patch_id VARCHAR,
	ids INTEGER,
	olevel INTEGER
);

CREATE TABLE IF NOT EXISTS ANSWER_OPTIONS (
	id INTEGER primary key autoincrement not null,
	optionsId INTEGER,
	questionId INTEGER,
	options VARCHAR,
	optionsRemarks VARCHAR,
	problemId INTEGER,
	isSave INTEGER
);

CREATE TABLE IF NOT EXISTS FINDING_DETAIL (
	id INTEGER primary key autoincrement not null,
	findIngId INTEGER,
	questionId INTEGER,
	choiceOptions VARCHAR,
	fillOptions VARCHAR,
	attachment VARCHAR
);

CREATE TABLE IF NOT EXISTS FINDINGS (
	id INTEGER primary key autoincrement not null,
	questionnaireId INTEGER,
	resultId INTEGER,
	investigatorId INTEGER,
	investigatorName VARCHAR,
	investigatorPhoneno VARCHAR,
	investigatorOrgPath VARCHAR,
	startDate VARCHAR,
	endDate VARCHAR,
	adressId VARCHAR,
	adress VARCHAR,
	lonLat VARCHAR
);

CREATE TABLE IF NOT EXISTS QUESTION (
	id INTEGER primary key autoincrement not null,
	questionId INTEGER,
	questionnaireId INTEGER,
	questionNum VARCHAR,
	level INTEGER,
	questionDiscriminate INTEGER,
	topic VARCHAR,
	remarks VARCHAR,
	chapterId INTEGER,
	isAnswer INTEGER
);

CREATE TABLE IF NOT EXISTS QUESTIONNAIRE (
	id INTEGER primary key autoincrement not null,
	questionid INTEGER,
	name VARCHAR,
	explain VARCHAR,
	startDate VARCHAR,
	endDate VARCHAR,
	numbers INTEGER,
	questionnaireState INTEGER,
	findingState INTEGER,
	cycle INTEGER,
	upCopies INTEGER
);

CREATE TABLE IF NOT EXISTS SURVEY_ADRESS (
	id INTEGER primary key autoincrement not null,
	surveyAdressId INTEGER,
	questionnaireId INTEGER,
	adress VARCHAR
);

CREATE TABLE IF NOT EXISTS EAXM_ANSWER_OPTIONS (
	id INTEGER primary key autoincrement not null,
	optionsId INTEGER,
	questionId INTEGER,
	options VARCHAR,
	isRight VARCHAR,
	scores INTEGER
);

CREATE TABLE IF NOT EXISTS EXAMINATION (
	id INTEGER primary key autoincrement not null,
	examinationId INTEGER,
	title VARCHAR,
	explain VARCHAR,
	startDate VARCHAR,
	endDate VARCHAR,
	releaseDate VARCHAR,
	state INTEGER,
	record VARCHAR
);

CREATE TABLE IF NOT EXISTS EXAM_QUESTION (
	id INTEGER primary key autoincrement not null,
	questionId INTEGER,
	paperId INTEGER,
	paperNum INTEGER,
	questionLevel INTEGER,
	questionsDif INTEGER,
	topic VARCHAR,
	remarks VARCHAR,
	mulChoiceStandard INTEGER,
	answer VARCHAR,
	score INTEGER
);

CREATE TABLE IF NOT EXISTS EXAM_RESULT (
	id INTEGER primary key autoincrement not null,
	resultId INTEGER,
	paperId INTEGER,
	paperNum INTEGER,
	examUserId VARCHAR,
	examUserName VARCHAR,
	examPhoneno VARCHAR,
	paperSubmitTime VARCHAR,
	examTime VARCHAR,
	examUserOrgPath VARCHAR,
	score VARCHAR
);

CREATE TABLE IF NOT EXISTS EXAM_RESULT_DETAIL (
	id INTEGER primary key autoincrement not null,
	resultId INTEGER,
	questionId INTEGER,
	optionIds VARCHAR,
	fillContent VARCHAR,
	attachmentUrl VARCHAR
);

CREATE TABLE IF NOT EXISTS DOUBLE_TASK_MANAGER (
	id INTEGER primary key autoincrement not null,
	menuId INTEGER,
	dataId INTEGER
);

CREATE TABLE IF NOT EXISTS IS_LEAVE (
	id INTEGER primary key autoincrement not null,
	type VARCHAR,
	name VARCHAR,
	maxDays VARCHAR
);
CREATE TABLE IF NOT EXISTS AF_LEAVE_INFO (
	id INTEGER primary key autoincrement not null,
	userId VARCHAR,
	name VARCHAR,
	status VARCHAR,
	statusName VARCHAR,
	marks VARCHAR,
	userName VARCHAR,
	startTime VARCHAR,
	endTime VARCHAR,
	imageUrl VARCHAR,
	days VARCHAR,
	hours VARCHAR,
	leaveName VARCHAR,
	type VARCHAR,
	imageName VARCHAR,
	msgKey VARCHAR,
	auditComment VARCHAR,
	leaveDay VARCHAR,
	orgName VARCHAR
);
CREATE TABLE IF NOT EXISTS WORK_PLAN_MODLE (
	id INTEGER primary key autoincrement not null,
	plan_id INTEGER,
	plan_title VARCHAR,
	plan_content VARCHAR,
	plan_note VARCHAR,
	important_level INTEGER,
	rush_level INTEGER
);
CREATE TABLE IF NOT EXISTS PLAN_DATA (
	id INTEGER primary key autoincrement not null,
	plan_id INTEGER,
	userName VARCHAR,
	userCode VARCHAR,
	orgName VARCHAR,
	planTitle VARCHAR,
	planData VARCHAR,
	startDate VARCHAR,
	endDate VARCHAR
);
CREATE TABLE IF NOT EXISTS PLAN_ASSESS (
	id INTEGER primary key autoincrement not null,
	plan_id INTEGER,
	user_id VARCHAR,
	user_name VARCHAR,
	marks VARCHAR
);
CREATE TABLE IF NOT EXISTS FUNC_CACHE (
id INTEGER primary key autoincrement not null,
modleId VARCHAR,
funcId VARCHAR,
paramValue VARCHAR,
paramId VARCHAR
);
CREATE TABLE IF NOT EXISTS WORK_SUMMARY_DATA (
id INTEGER primary key autoincrement not null,
plan_id VARCHAR,
sub_time VARCHAR,
sum_date VARCHAR,
sum_marks VARCHAR,
user_code VARCHAR,
user_id VARCHAR,
user_name VARCHAR,
audit_user_name VARCHAR,
audit_type VARCHAR,
audit_status VARCHAR,
audit_marks VARCHAR,
pic_url VARCHAR,
record_url VARCHAR
);
CREATE TABLE IF NOT EXISTS ADV_PICTURE (
id INTEGER primary key autoincrement not null,
sortId Integer,
pictureName VARCHAR,
url VARCHAR,
sdPath VARCHAR
);


-- Home页石化模块
CREATE TABLE IF NOT EXISTS MAIN_SHIHUA_MENU (
	id INTEGER primary key autoincrement not null,
	folderName VARCHAR,
	icon VARCHAR,
	list INTEGER
);

