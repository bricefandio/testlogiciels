package fr.troisIl.evaluation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ProductServiceTest {

    private Database db = null;
    private ProductService productService;


    private int countBefore = 0;

    @Before
    public void setUp() throws SQLException {
        String testDatabaseFileName = "product.db";

        // reset la BDD avant le test
        File file = new File(testDatabaseFileName);
        file.delete();

        db = new Database(testDatabaseFileName);
        db.createBasicSqlTable();

        productService = new ProductService(db);

        countBefore = count();
    }

    /**
     * Compte les produits en BDD
     *
     * @return le nombre de produit en BDD
     */
    private int count() throws SQLException {
        ResultSet resultSet = db.executeSelect("Select count(*) from Product");
        assertNotNull(resultSet);
        return resultSet.getInt(1);
    }

    @Test
    public void testInsertNull() {
        try {
            productService.insert(null);
            fail("Le test aurait du planter");
        } catch (RuntimeException e) {
            assertEquals("Le produit ne peut pas être null", e.getMessage());
        }
    }

    @Test
    public void testInsertNullLabel() {
        try {
            productService.insert(new Product());
            fail("Le test aurait du planter");
        } catch (RuntimeException e) {
            assertEquals("Le libellé du produit est requis", e.getMessage());
        }
    }

    @Test
    public void testUpdateNull() {
        try {
            productService.update(null);
            fail("Le test aurait du planter");
        } catch (RuntimeException e) {
            assertEquals("Le produit ne peut pas être null", e.getMessage());
        }
    }

    @Test
    public void testUpdateNullLabel() {
        try {
            productService.update(new Product());
            fail("Le test aurait du planter");
        } catch (RuntimeException e) {
            assertEquals("Le libellé du produit est requis", e.getMessage());
        }
    }

    @Test
    public void testUpdateNullId() {
        Product product = new Product();
        product.setLabel("COUCOUUUUU");
        try {
            productService.update(product);
            fail("Le test aurait du planter");
        } catch (RuntimeException e) {
            assertEquals("L'identifiant du produit est requis pour une modification", e.getMessage());
        }
    }

    @Test
    public void testUpdateNullQuantity() {
        Product product = new Product();
        product.setLabel("COUCOUUUUU");
        product.setId(1);
        try {
            productService.update(product);
            fail("Le test aurait du planter");
        } catch (RuntimeException e) {
            assertEquals("La quantité ne peut être nulle", e.getMessage());
        }
    }


    @Test
    public void testUpdateNotFoundProduct() {
        Product product = new Product();
        product.setLabel("COUCOUUUUU");
        product.setQuantity(2);
        product.setId(99999);
        try {
            productService.update(product);
            fail("Le test aurait du planter");
        } catch (RuntimeException e) {
            assertEquals("Le produit n'a pas été trouvé en BDD", e.getMessage());
        }
    }

    @Test
    public void testFindUnnexistantProduct() {
        try {
            productService.findById(99999);
            fail("Le test aurait du planter");
        } catch (RuntimeException e) {
            assertEquals("Le produit n'a pas été trouvé en BDD", e.getMessage());
        }
    }

    @Test
    public void testFindIdNull() {
        try {
            productService.findById(null);
            fail("Le test aurait du planter");
        } catch (RuntimeException e) {
            assertEquals("L'id du produit recherché est requis", e.getMessage());
        }
    }

    @Test
    public void testDeleteUnnexistantProduct() {
        try {
            productService.delete(99999);
            fail("Le test aurait du planter");
        } catch (RuntimeException e) {
            assertEquals("Le produit n'a pas été trouvé en BDD", e.getMessage());
        }
    }

    @Test
    public void testDeleteIdNull() {
        try {
            productService.delete(null);
            fail("Le test aurait du planter");
        } catch (RuntimeException e) {
            assertEquals("L'id du produit à supprimer est requis", e.getMessage());
        }
    }


}
