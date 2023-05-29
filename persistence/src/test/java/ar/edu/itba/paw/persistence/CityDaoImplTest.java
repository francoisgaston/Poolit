package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CityDaoImplTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CityHibernateDao cityDao;

    private static final long PROVINCE_ID = 1;

    private static final long CITY_ID = 1;

    private static final String CITY_NAME = "Recoleta";

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("INSERT INTO provinces VALUES(?,?)",PROVINCE_ID,"CABA");
    }

    @Test
    @Rollback
    public void testFindByIdEmpty(){
        //No setup

        //Execute
        Optional<City> city = cityDao.findCityById(1);

        //Assert
        Assert.assertFalse(city.isPresent());
    }

    @Rollback
    @Test
    public void testFindByIdPresent(){
        //SetUp
        jdbcTemplate.update("INSERT INTO cities values (?,?,?)",CITY_ID,CITY_NAME,PROVINCE_ID);

        //Execute
        Optional<City> city = cityDao.findCityById(CITY_ID);

        //Assert
        Assert.assertTrue(city.isPresent());
        Assert.assertEquals(CITY_ID,city.get().getId());
        Assert.assertEquals(CITY_NAME,city.get().getName());
        Assert.assertEquals(PROVINCE_ID,city.get().getProvinceId());
    }

    @Rollback
    @Test
    public void testFindByProvinceId(){
        //SetUp
        final City[] cities = new City[]{
                new City(1,"Recoleta",PROVINCE_ID),
                new City(2,"Parque Patricios",PROVINCE_ID),
                new City(3,"San Nicolas",PROVINCE_ID),
                new City(4,"Palermo",PROVINCE_ID),
                new City(5,"Puerto Madero",PROVINCE_ID)
        };
        for(City city: cities){
            jdbcTemplate.update("INSERT INTO cities values (?,?,?)",city.getId(),city.getName(),city.getProvinceId());
        }
        final int count = JdbcTestUtils.countRowsInTable(jdbcTemplate,"cities");

        //Execute
        List<City> ans = cityDao.getCitiesByProvinceId(PROVINCE_ID);

        //Assert
        for(City city: cities){
            Assert.assertTrue(ans.stream().anyMatch(c -> c.getId() == city.getId() && c.getName().equals(city.getName()) && c.getProvinceId()==city.getProvinceId()));
        }
        Assert.assertEquals(cities.length,ans.size());
        Assert.assertEquals(count,JdbcTestUtils.countRowsInTable(jdbcTemplate,"cities"));
    }

}
