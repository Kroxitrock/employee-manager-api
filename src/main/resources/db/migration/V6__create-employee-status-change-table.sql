CREATE TABLE PUBLIC.EMPLOYEE_STATUS_CHANGE (
	ID IDENTITY NOT NULL,
	OLD_STATUS_ID BIGINT NOT NULL,
	NEW_STATUS_ID BIGINT NOT NULL,
	REASON VARCHAR(512),
	EMPLOYEE_ID BIGINT NOT NULL,
	DATE_CREATED TIMESTAMP NOT NULL,
	CONSTRAINT EMPLOYEE_STATUS_CHANGE_PK PRIMARY KEY (ID),
	CONSTRAINT NEW_EMPLOYEE_STATUS_CHANGE_EMPLOYEE_STATUS_FK FOREIGN KEY (NEW_STATUS_ID) REFERENCES PUBLIC.EMPLOYEE_STATUS(ID),
	CONSTRAINT OLD_EMPLOYEE_STATUS_CHANGE_EMPLOYEE_STATUS_FK_ FOREIGN KEY (OLD_STATUS_ID) REFERENCES PUBLIC.EMPLOYEE_STATUS(ID),
	CONSTRAINT EMPLOYEE_STATUS_CHANGE_EMPLOYEE_FK FOREIGN KEY (EMPLOYEE_ID) REFERENCES PUBLIC.EMPLOYEE(ID)
);
