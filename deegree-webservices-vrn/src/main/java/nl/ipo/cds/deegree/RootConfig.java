/**
 *
 */
package nl.ipo.cds.deegree;

import nl.ipo.cds.dao.ManagerDao;

import nl.ipo.cds.deegree.extension.vrnfilter.VRNFilterSQLFeatureStoreProvider;
import nl.ipo.cds.deegree.security.DeegreeVrnWebSecurityConfigurerAdapter;
import nl.ipo.cds.properties.ConfigDirPropertyPlaceholderConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;

/**
 * @author reinoldp
 */
@Configuration
@Import({DeegreeVrnWebSecurityConfigurerAdapter.class, DataSourceConfig.class, VRNFilterSQLFeatureStoreProvider.class})
@ImportResource(value = {"classpath:nl/ipo/cds/dao/dao-applicationContext.xml", "classpath:META-INF/nl/ipo/cds/deegree/extension/vrnfilter/applicationContext.xml"})
public class RootConfig {

    /**
     * <bean id="propertyPlaceholderConfigurer" class="nl.ipo.cds.properties.ConfigDirPropertyPlaceholderConfigurer"/>
     *
     * @return
     */
    @Bean
    public static ConfigDirPropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        final ConfigDirPropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new ConfigDirPropertyPlaceholderConfigurer();
        return propertyPlaceholderConfigurer;
    }

//    @Autowired
//    private VRNFilterSQLFeatureStoreProvider vrnFilterSQLFeatureStoreProvider;

    @Autowired
    private BaseLdapPathContextSource ldapServer;

    private ManagerDao managerDao;

    @Autowired
    public void setManagerDao(ManagerDao managerDao) {
        VRNFilterSQLFeatureStoreProvider.setManagerDao ( managerDao);
    }

    @Bean
    public LdapTemplate ldapTemplate() throws Exception {
        return new LdapTemplate(ldapServer);
    }
}
