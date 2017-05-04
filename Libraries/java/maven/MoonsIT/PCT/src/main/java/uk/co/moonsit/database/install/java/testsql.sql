--insert into scores (ID,Model,Fidelity,SimulatedTime,FidelityScore,TimeScore,Score,Level,TargetX,TargetY,ConstraintKey) values ( '20170221-19-15-04.265','005-008-QMBHWShovelling',0.9223,0.236,680516,141000,821516,13.0,-0.5900,0,'13.0_-0.5900_0_1.0_-150.0,_0.01_10.0_0.0_0.0_0.0_2.0_0.2_0.0_0.0_0.0_2.0_0.02_0.0_0.0_0.001_0.0,_0.5_0.0,_5000.0_10000.0_0.01,0.01,0.001,_1.0_100.0_0.02_0.0_0.0_0.001_10.0_');

--SELECT	* FROM QUANTUM.SCORES ;

SELECT 	ID, Level, 	Score, TimeScore,	Fidelity, SimulatedTime,	ConstraintKey, Model FROM QUANTUM.SCORES 
--SELECT	* FROM QUANTUM.SCORES 
where level=13  
--and id = '20170426-21-07-16.754'
--and id > '20170503-16-10-00.170'
--and model = '005-013-QMBHWTunnelingReorg'
--and model ='005-014-QMBHWTunnelMultiReorg'
and model = '005-015-QMBHWTunnelScoreReorg'
--and model = '005-002-QMPositionSingleIntegrator'
--and model = '005-011-QMBHWTunneling'
order by level, score desc, id desc;
-- FETCH FIRST 100 ROWS ONLY;



/*
SELECT PARAMETERS.ID,  SCORES.Score,  SCORES.Model, PARAMETERS.functionname, PARAMETERS.Value
FROM PARAMETERS INNER JOIN SCORES
ON PARAMETERS.ID = SCORES.ID
WHERE PARAMETERS.functionname = 'ShiftRef' and PARAMETERS.Value > 0.8
order by SCORES.score desc;
*/

--SELECT * FROM QUANTUM.PARAMETERS where functionname = 'MoveRef' and value = 0.35  --ID > '2017042-14-51-05.382' 
--order by functionname, parameter;

--SELECT * FROM QUANTUM.PARAMETERS where ID = '20170503-16-11-27.973' 
--order by functionname, parameter;

--SELECT * FROM QUANTUM.PARAMETERS where ID = '20170423-15-35-41.590' 
--order by functionname, parameter;

--SELECT * FROM QUANTUM.PARAMETERS where  functionname = 'OffsetError'
--order by value,functionname, parameter;

--SELECT * FROM QUANTUM.PARAMETERS where  functionname = 'ShiftRef' and value > 0.4197 and value < 0.4199
--order by value,functionname, parameter desc;

--SELECT * FROM QUANTUM.PARAMETERS where  functionname = 'MoveOutput' and value > 0.0030099 and value < 0.0032
--order by value,functionname, parameter desc;

--and value != 0 order by functionname, parameter;

--call removeScores('20170412-14-49-49.434');


--DELETE FROM PARAMETERS  WHERE id='20170503-16-11-27.973';
--DELETE FROM Scores  WHERE id='20170503-16-11-27.973';


-- 20170222-15-06-27.070
-- 20170222-15-06-04.171
-- 1
--CALL SYSCS_UTIL.SYSCS_EXPORT_QUERY ('SELECT * from PARAMETERS where ID = ''20170207-13-11-34.048''' ,   'c:/tmp/exportdata/1.csv',  ',', null,  NULL);
-- 2
--CALL SYSCS_UTIL.SYSCS_EXPORT_QUERY ('SELECT * from PARAMETERS where ID = ''20170207-13-33-36.235''' ,   'c:/tmp/exportdata/2.csv',  ',', null,  NULL);
-- 3
--CALL SYSCS_UTIL.SYSCS_EXPORT_QUERY ('SELECT * from PARAMETERS where ID = ''20170208-12-47-16.179''' ,   'c:/tmp/exportdata/3.csv',  ',', null,  NULL);
-- 4
--CALL SYSCS_UTIL.SYSCS_EXPORT_QUERY ('SELECT * from PARAMETERS where ID = ''20170207-15-50-29.876''' ,   'c:/tmp/exportdata/4.csv',  ',', null,  NULL);
-- 5
--CALL SYSCS_UTIL.SYSCS_EXPORT_QUERY ('SELECT * from PARAMETERS where ID = ''20170208-13-16-34.487''' ,   'c:/tmp/exportdata/5.csv',  ',', null,  NULL);

--insert into scores (ID,Model,Fidelity,SimulatedTime,FidelityScore,TimeScore,Score,Level,TargetX,TargetY,ConstraintKey) values ( '20170206-17-27-29.377','005-006-PVA-DampenControl',0.9974,0.058,795845,185500,981345,1.0,0.0750,0,'1.0,0.0750,0,2.4,0.022,0.0,0.0,0.0,0.1,0.5,0.1,5000.0,10000.0,2.0,0.1,0.0,0.0,0.0,0.1,1000.0,10000.0,10.0,');


 --ALTER TABLE scores ALTER COLUMN ConstraintKey  SET DATA TYPE varchar(1024);

--DELETE FROM PARAMETERS  WHERE 1=1;
--DELETE FROM SCORES  WHERE 1=1;


--update scores set ID='20170202-14-00-42.332',Model='005-002-QMPositionSingleIntegrator',Fidelity=0.9974,SimulatedTime=0.232,FidelityScore=795866,TimeScore=142000,Score=937866,TargetX=0.3750,TargetY=0 where ConstraintKey='1.0-0.3750-0-5.0-1000.0-50.0-';


--insert into scores (ID,Model,Fidelity,SimulatedTime,FidelityScore,TimeScore,Score,Level,TargetX,TargetY,ConstraintKey) values ( '20170202-13-59-49.150','005-002-QMPositionSingleIntegrator',0.9974,0.232,795865,142000,937865,1.0,0.3750,0,'1.0-0.3750-0-5.0-1000.0-50.0-');
--insert into scores (ID,Model,Fidelity,SimulatedTime,FidelityScore,TimeScore,Score,Level,TargetX,TargetY,ConstraintKey) values ( '20170202-14-00-07.233','005-002-QMPositionSingleIntegrator',0.9974,0.232,795866,142000,937866,1.0,0.3750,0,'1.0-0.3750-0-5.0-1000.0-50.0-');

