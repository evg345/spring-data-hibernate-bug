package bugreport;

import config.TestH2Database;
import models.Prima;
import models.Secunda;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import repositories.PrimaRepository;

/**
 * In case of Hibernate version
 * <ul>
 * <li>5.2.14.Final</li>
 * <li>5.2.15.Final</li>
 * <li>5.2.16.Final</li>
 * </ul>
 * this test will fail with
 * <pre>
 * org.hibernate.id.IdentifierGenerationException: attempted to assign id from null one-to-one property [models.Secunda.parent]
 * </pre>
 *
 * Good versions are
 * <ul>
 * <li>5.2.12.Final</li>
 * <li>5.2.13.Final</li>
 * </ul>
 *
 * @author localEvg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestH2Database.class)
@Transactional
public class OneToOnePropertyTest {

    private Long primaId;

    @Autowired
    PrimaRepository repoPrima;

    @Before
    public void prepare() {

        Prima prim = new Prima();
        prim.setOptionalData(null); // <-- main line

        prim = repoPrima.save(prim);
        this.primaId = prim.getId();

        Assert.assertNotNull(this.primaId);
        System.out.println("Object saved with OptionalData = null // Prima.id = " + this.primaId.toString());
    }

    @Test
    public void updateWithNonNull() {
        Assert.assertNotNull(this.primaId);

        // we emulate object recieved from json
        Prima prim = new Prima();
        prim.setId(this.primaId);
        {
            Secunda sec = new Secunda();
            sec.setParent(prim); // <-- not null !
            prim.setOptionalData(sec);
        }

        Prima savedPrima = repoPrima.save(prim); // <-- exception here

        Assert.assertNotNull(savedPrima);
        Assert.assertNotNull(savedPrima.getId());
        Assert.assertEquals(savedPrima.getId(), this.primaId);
        Assert.assertNotNull(savedPrima.getOptionalData());
        Assert.assertEquals(savedPrima.getId(), savedPrima.getOptionalData().getId());

    }

}
