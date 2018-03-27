# spring-data-hibernate-bug

Test case to reproduce bug for OneToOne property in Spring+Hibernate enviroment.

 it seems that version *Hibernate* 5.2.14.Final and later are NOT compatible with *Spring-Data* 2.0.5 (see test case) !!
*  5.2.12.Final   = GOOD
*  5.2.13.Final   = GOOD
*  5.2.14.Final   = Exception
*  5.2.15.Final   = Exception
*  5.2.16.Final   = Exception

