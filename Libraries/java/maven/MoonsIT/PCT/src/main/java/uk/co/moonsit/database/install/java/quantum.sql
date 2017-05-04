

--drop table Scores;


CREATE TABLE Scores (
	ID varchar (21) NOT NULL ,
	Level int NOT NULL,
	Score int NOT NULL,
	FidelityScore int NOT NULL,
	TimeScore int NOT NULL,
	Fidelity float NOT NULL,
	SimulatedTime float NOT NULL,
	TargetX float NOT NULL,
	TargetY float NOT NULL,
	Model varchar (100) NOT NULL,
	ConstraintKey varchar (512) NOT NULL,
        PRIMARY KEY  (Model, ConstraintKey)
);

-- ALTER TABLE scores ALTER COLUMN ConstraintKey  SET DATA TYPE varchar(1024);


--drop table Parameters;

CREATE TABLE Parameters (
	ID varchar (21) NOT NULL ,
	FunctionName varchar (50) NOT NULL,
	Parameter varchar (20) NOT NULL,
	Value float NOT NULL
);

