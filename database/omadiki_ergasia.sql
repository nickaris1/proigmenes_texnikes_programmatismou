PRAGMA foreign_keys = ON; 
PRAGMA encoding="UTF-8";
BEGIN TRANSACTION;
DROP TABLE IF EXISTS "PROPERTY";
CREATE TABLE IF NOT EXISTS "PROPERTY" (
    "id" integer				NOT NULL,
    "listed_price" integer,
    "tm" integer,
    "type" varchar(255),
    "Road" varchar(255),
    "Address_num" integer,
    "Floor" integer,
    "Availability" boolean	DEFAULT FALSE	NOT NULL,
    "owner_afm" integer						NOT NULL,
    "area_code" integer,
    CONSTRAINT "PROPERTY_OWNER_FK" FOREIGN KEY("owner_afm") REFERENCES "OWNER"("AFM")				ON DELETE CASCADE		ON UPDATE CASCADE,
    CONSTRAINT "PROPERTY_LOCATION_FK" FOREIGN KEY("area_code") REFERENCES "LOCATION"("Area_code")	ON DELETE SET NULL		ON UPDATE CASCADE,
    PRIMARY KEY("id")
);

DROP TABLE IF EXISTS "LOCATION";
CREATE TABLE IF NOT EXISTS "LOCATION" (
    "Area_code" integer				NOT NULL,
    "City" varchar(255)				NOT NULL,
    "Area" varchar(255)				NOT NULL,
    "County" varchar(255)			NOT NULL,
    PRIMARY KEY("Area_code")
);

DROP TABLE IF EXISTS "OWNER";
CREATE TABLE IF NOT EXISTS "OWNER" (
    "AFM" integer				NOT NULL,
    "Fname" varchar(255)		NOT NULL,
    "Lname" varchar(255)		NOT NULL,
    "Phone" integer		NOT NULL,
    CONSTRAINT "PhoneLength"  CHECK("Phone" >= 0000000000 AND "Phone" < 10000000000),
    PRIMARY KEY("AFM")
);

DROP TABLE IF EXISTS "SALE";
CREATE TABLE IF NOT EXISTS "SALE" (
    "id" integer		NOT NULL,
    "date" date,
    "price" real,
    "tm" real,
    "price_tm" real DEFAULT NULL,

    "rental" boolean DEFAULT FALSE,

    "warranty" integer DEFAULT NULL,
    "startdate" date DEFAULT NULL,
    "enddate" date DEFAULT NULL,

    "property_id" integer NOT NULL,
    CONSTRAINT "PROPERTY_FK" FOREIGN KEY("property_id") REFERENCES "PROPERTY"("id") ON DELETE CASCADE	ON UPDATE CASCADE,

    PRIMARY KEY("id")
);

----
INSERT INTO "LOCATION" ("Area_code", "City", "Area", "County") VALUES
    (15125, "Αθήνα", 		"ΜΑΡΟΥΣΙ",      "Ελλάδα"),
    (15127, "Αθήνα", 		"ΜΕΛΙΣΣΙΑ",     "Ελλάδα"),
    (15451, "Αθήνα", 		"Ν.ΨΥΧΙΚΟ",     "Ελλάδα"),
    (17561, "Αθήνα", 		"Π.ΦΑΛΗΡΟ",     "Ελλάδα"),
    (17562, "Αθήνα", 		"Π.ΦΑΛΗΡΟ",     "Ελλάδα"),
    (26224, "Πάτρα", 		"ΨΗΛΑ ΑΛΩΝΙΑ",  "Ελλάδα"),
    (26442, "Πάτρα", 		"ΑΓ. ΣΟΦΙΑ",    "Ελλάδα"),
    (26221, "Πάτρα", 		"ΠΑΤΡΑ",	    "Ελλάδα"),
    (41222, "Λάρισα",		"ΠΑΠΑΣΤΑΥΡΟΥ",  "Ελλάδα"),
    (54351, "Θεσσαλονίκη", 	"ΑΝΩ ΤΟΥΜΠΑ", 	"Ελλάδα"),
    (55534, "Θεσσαλονίκη", 	"ΠΥΛΑΙΑ", 		"Ελλάδα");

----
INSERT INTO "OWNER" ("AFM", "Fname", "Lname", "Phone") VALUES
    (0000, "Sinus", 		"Lebastian", 	4200617621),
    (0001, "Pranav", 		"Farley", 		7830228455),
    (0002, "Anisha", 		"Macdonald", 	7330515533),
    (0003, "Elisabeth", 	"Haas",		 	0170314819),
    (0004, "Teresa", 		"Hurst", 		8630688413),
    (0005, "Roksana", 		"Burnett", 		2490426926),
    (0006, "Eboni", 		"Lees", 		7360915355),
    (0007, "Ella-Grace",	"Hebert", 		0060303109),
    (0008, "Ammaarah", 		"Jordan", 		9460411113),
    (0009, "Luna", 			"Amos", 		4150276408);


----
INSERT INTO "PROPERTY" ("id", "listed_price", "tm", "type", "Road", "Address_num", "Floor", "Availability", "owner_afm", "area_code") VALUES
    (0, 125000,	104,	"ΔΙΑΜΕΡΙΣΜΑ",   "ΜΑΡΚΟΥ ΜΠΟΤΣΑΡΗ",	   	23,		5,	TRUE, 0008, 17562), 	--sale apt
    (1, 103000,	81,		"ΔΙΑΜΕΡΙΣΜΑ",   "ΑΝΔΡΟΥ",				 7,		2,	TRUE, 0004, 54351), 	--sale apt
    (2, 5000,		550,	"ΒΙΛΑ",         "ΟΛΥΜΠΟΥ", 			   	15,	 NULL,	TRUE, 0004, 55534), 	--rent villa
    (3, 380,		32,		"ΓΚΑΡΣΟΝΙΕΡΑ",  "ΚΑΝΑΡΗ", 				 2,		0,	TRUE, 0000, 41222), 	--rent studio
    (4, 117000,	467,	"ΟΙΚΟΠΕΔΟ",     "ΑΓΙΩΝ ΠΑΝΤΩΝ", 	   	12,  NULL,	TRUE, 0003, 26224), 	--sale land
    (5, 35000,		139,	"ΟΙΚΟΠΕΔΟ",     "ΚΩΝΣΤΑΝΤΙΝΟΥΠΟΛΕΩΣ", 	 7,	 NULL,	TRUE, 0001, 15125), 	--sale land
    (6, 500,		62,		"ΓΡΑΦΕΙΟ",      "ΜΙΜΟΖΑΣ", 			   	38,		4,	TRUE, 0008, 26442), 	--rent office
    (7, 730,		90,		"ΔΙΑΜΕΡΙΣΜΑ",   "ΔΙΑΓΟΡΑ", 			     6,		2,	TRUE, 0009, 15127), 	--rent apt
    (8, 800,		280,	"ΚΑΤΑΣΤΗΜΑ",    "ΑΓΙΟΥ ΔΗΜΗΤΡΙΟΥ", 	   	11,		0,	TRUE, 0002, 15451), 	--rent store
    (9, 230000,	275,	"ΜΟΝΟΚΑΤΟΙΚΙΑ", "ΜΙΛΤΙΑΔΟΥ", 			21,  NULL,	TRUE, 0003, 41222), 	--sale house
    (10, 2000,		350,	"ΓΡΑΦΕΙΟ",      "ΑΜΕΡΙΚΗΣ",				 1,		1,	TRUE, 0007, 41222), 	--rent office
    (11, 32000,		200,	"ΟΙΚΟΠΕΔΟ",     "ΑΧΑΡΝΩΝ", 			   132,	 NULL,	TRUE, 0009, 17561), 	--sale land
    (12, 100000,	60,		"ΓΡΑΦΕΙΟ",      "ΑΓΙΑΣ ΕΙΡΗΝΗΣ", 		 6,		2,	TRUE, 0007, 41222), 	--sale office
    (13, 10000000,	4600,	"ΞΕΝΟΔΟΧΕΙΟ",   "ΚΟΛΟΚΟΤΡΩΝΗ", 			49,	 NULL,	TRUE, 0005, 26221), 	--sale hotel
    (14, 450000,	130,	"ΔΙΑΜΕΡΙΣΜΑ",   "ΠΟΝΤΟΥ", 				12,		3,	TRUE, 0001, 15127); 	--sale apt

----

INSERT INTO "SALE" ("id", "price", "tm", "date", "rental", "warranty", "startdate", "enddate", property_id) VALUES
    (1, 125000,	104,	NULL,           false, NULL, NULL, NULL, 1),
    (2, 103000,	81,		NULL,           false, NULL, NULL, NULL, 2),
    (3, 117000,	467,	"2021-03-23",   false, NULL, NULL, NULL, 3),
    (4, 35000,     139,	NULL,           false, NULL, NULL, NULL, 4),
    (5, 230000,	275,	"2021-11-13",   false, NULL, NULL, NULL, 5),
    (6, 32000,     200,	NULL,           false, NULL, NULL, NULL, 6),
    (7, 700,	    60,		"2021-02-05",   true, 700, "2021-02-05", "2022-02-05", 7),
    (8, 10000000,	4600,	"2019-09-13",   false, NULL, NULL, NULL, 8),
    (9, 45000,     130,	NULL,           false, NULL, NULL, NULL, 9),
    (10, 30000,	    60,		"2022-03-01",   false, 700, NULL, NULL, 7);

COMMIT;
