
INSERT INTO esea_team (tID,tName) VALUES( 82, 'Red' );
INSERT INTO esea_team (tID,tName) VALUES( 66, 'Blu' );

INSERT INTO esea_player(pID,pName, tID) VALUES(104, 'happs', 82);
INSERT INTO esea_player(pID,pName, tID) VALUES(115, 'Steely', 66);

INSERT INTO esea_match(mID, pID,stat_A,stat_D,stat_UC) VALUES(1,104,44,3,9);
INSERT INTO esea_match(mID, pID,stat_DOM,stat_D,stat_P) VALUES(1,115,44,2,90);

