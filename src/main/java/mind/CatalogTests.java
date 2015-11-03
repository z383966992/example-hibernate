/*package mind;

import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import datasource.DataSourceContextHolder;
import datasource.DataSourceType;

public class CatalogTests extends AbstractDependencyInjectionSpringContextTests {

	private Catalog catalog;

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public void testDataSourceRouting() throws Exception{
			DataSourceContextHolder.setCustomerType(DataSourceType.MASTER);
			List<Item> masterItems = catalog.getItems();
			System.out.println("Master items: " + masterItems);

			DataSourceContextHolder.setCustomerType(DataSourceType.SLAVE);
			List<Item> slaveItems = catalog.getItems();
			System.out.println("slave items: " + slaveItems);
	}

	protected String[] getConfigLocations() {
		return new String[] { "/spring/spring-config.xml" };
	}
}
*/