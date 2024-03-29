DROP TABLE IF EXISTS esea_match;
DROP TABLE IF EXISTS esea_player;
DROP TABLE IF EXISTS esea_team;

CREATE TABLE esea_team(
	tID	INT(11)		NOT NULL	PRIMARY KEY
	COMMENT 'Esea Team Id',
	tName	VARCHAR(24)	NOT NULL
	COMMENT 'Team Name',
	game   VARCHAR(5) NOT NULL
	COMMENT 'Game',
	league VARCHAR(14) NOT NULL
	COMMENT 'League Name'
	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE esea_player(
	pID 	INT(11)	 	NOT NULL	PRIMARY KEY
	COMMENT 'Esea Player Id',
	pName 	VARCHAR(24)	NOT NULL
	COMMENT 'Player Name',
	tID	 INT(11)
	COMMENT 'Esea Team Id',
	FOREIGN KEY (tID) REFERENCES esea_team(tID)
		ON DELETE CASCADE
		ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE esea_match(
	mID	INT(11)		NOT NULL
	COMMENT 'Esea Match Id',

	pID	INT(11)		NOT NULL
	COMMENT 'Esea Player Id',

	stat_P		SMALLINT	DEFAULT 0
	COMMENT 'Points',

	stat_PPM	FLOAT		DEFAULT 0.0
	COMMENT 'Points Per Minute',

	stat_DMG	SMALLINT	DEFAULT 0
	COMMENT 'Damage',
	
	stat_DDM	FLOAT		DEFAULT 0.0
	COMMENT 'Damge Per Minute',

	stat_F		SMALLINT	DEFAULT 0
	COMMENT 'Frags',

	stat_FPM	FLOAT		DEFAULT	0.0
	COMMENT 'Frage Per Minute',

	stat_A		SMALLINT	DEFAULT	0
	COMMENT 'Assists',

	stat_APM	FLOAT		DEFAULT 0.0
	COMMENT 'Assits Per Minute',

	stat_D		SMALLINT	DEFAULT 0
	COMMENT 'Deaths',

	stat_DPM	FLOAT		DEFAULT 0.0
	COMMENT 'Deaths Per Minute',

	stat_CPC	SMALLINT	DEFAULT 0
	COMMENT 'Capture Points Captured',

	stat_CPB	SMALLINT	DEFAULT 0
	COMMENT 'Capture Points Blocked',

	stat_DOM	TINYINT 	DEFAULT 0
	COMMENT 'Dominations',

	stat_REV	TINYINT 	DEFAULT 0
	COMMENT 'Revenges',

	stat_UC		TINYINT 	DEFAULT 0
	COMMENT 'Ubercharges',

	stat_UCD	TINYINT 	DEFAULT 0
	COMMENT 'Ubercharges Dropped',

	FOREIGN KEY (pID) REFERENCES esea_player(pID)
		ON DELETE CASCADE
		ON UPDATE CASCADE,

	INDEX ( mID, pID )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
