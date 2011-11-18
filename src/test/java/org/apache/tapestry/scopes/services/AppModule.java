package org.apache.tapestry.scopes.services;

import org.apache.tapestry.scopes.ScopesModule;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;

@SubModule(ScopesModule.class)
public class AppModule
{
    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration)
    {
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
    }
}
