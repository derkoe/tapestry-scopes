package org.apache.tapestry.scopes;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class ScopesModule
{
    public static void bind(ServiceBinder binder)
    {
    }

    public void contributeMarkupRenderer(OrderedConfiguration<MarkupRendererFilter> configuration,
                                         final AssetSource assetSource, final ThreadLocale threadLocale,
                                         final Environment environment, final Request request)
    {
        MarkupRendererFilter injectScopesScript = new MarkupRendererFilter()
        {
            public void renderMarkup(MarkupWriter writer, MarkupRenderer renderer)
            {
                JavaScriptSupport renderSupport = environment.peek(JavaScriptSupport.class);

                Asset validators = assetSource.getUnlocalizedAsset("org/apache/tapestry/scopes/tapestry-scopes.js");

                renderSupport.importJavaScriptLibrary(validators);

                renderSupport.addInitializerCall("scopesInit", request.getContextPath());

                renderer.renderMarkup(writer);
            }
        };

        configuration.add("InjectScopesScript", injectScopesScript, "after:*");
    }
}
