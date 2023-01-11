package fr.troisIl.evaluation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProductServiceIT {

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
    public void testInsert() throws SQLException {
        Product product = new Product();
        product.setLabel("Super label");
        Product retour = productService.insert(product);

        Assert.assertNotNull(retour);
        Assert.assertNotNull(retour.getId());
        Assert.assertEquals("Super label", retour.getLabel());
        Assert.assertEquals(0, (int) retour.getQuantity());

        // ne doit avoir créé que un produit
        Assert.assertEquals(countBefore + 1, count());

        // vérifie l'entrée en BDD
        ResultSet resultSet = db.executeSelect("Select * from product Where id = " + retour.getId());

        Assert.assertNotNull(resultSet);
        Assert.assertNotNull(resultSet.getInt("id"));
        Assert.assertEquals("Super label", resultSet.getString("label"));
        Assert.assertEquals(0, resultSet.getInt("quantity"));
    }

    @Test
    public void testUpdate() throws SQLException {
        Product product = new Product();
        product.setLabel("Super label");
        Product retour = productService.insert(product);

        // modifie le produit pour validation des champs
        retour.setQuantity(10);
        retour.setLabel("Produit modifié");
        int id = retour.getId();
        retour = productService.update(product);

        Assert.assertNotNull(retour);
        Assert.assertEquals(id, (int) retour.getId());
        Assert.assertEquals("Produit modifié", retour.getLabel());
        Assert.assertEquals(10, (int) retour.getQuantity());

        // ne doit avoir créé que un produit et que la mise a jour n'en n'a pas créé de nouveau
        Assert.assertEquals(countBefore + 1, count());

        // vérifie l'entrée en BDD
        ResultSet resultSet = db.executeSelect("Select * from product Where id = " + retour.getId());

        Assert.assertNotNull(resultSet);
        Assert.assertNotNull(resultSet.getInt("id"));
        Assert.assertEquals("Produit modifié", resultSet.getString("label"));
        Assert.assertEquals(10, resultSet.getInt("quantity"));
    }

    @Test
    public void testFindById() throws SQLException {
        Product product = new Product();
        product.setLabel("Super label");
        Product retour = productService.insert(product);

        // recherche le produit
        int id = retour.getId();
        retour = productService.findById(id);

        Assert.assertNotNull(retour);
        Assert.assertEquals(id, (int) retour.getId());
        Assert.assertEquals("Super label", retour.getLabel());
        Assert.assertEquals(0, (int) retour.getQuantity());
    }

    @Test
    public void testDelete() throws SQLException {
        Product product = new Product();
        product.setLabel("Super label");
        Product retour = productService.insert(product);

        // delete le produit
        int id = retour.getId();
        productService.delete(id);

        assertEquals(countBefore, count());

        ResultSet resultSet = db.executeSelect("Select * from product Where id = " + id);

        Assert.assertNotNull(resultSet);
        Assert.assertFalse(resultSet.next());
    }

    /* ##################################################### Validation des controles ############################## */


}
