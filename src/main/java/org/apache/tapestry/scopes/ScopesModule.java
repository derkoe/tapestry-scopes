package org.apache.tapestry.scopes;

import java.io.IOException;

import org.apache.tapestry.scopes.internal.WindowIdContextImpl;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.linktransform.ComponentEventLinkTransformer;
import org.apache.tapestry5.services.linktransform.PageRenderLinkTransformer;

public class ScopesModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(WindowIdContext.class, WindowIdContextImpl.class);
    }

    @Contribute(RequestHandler.class)
    public static final void addRequestFilter(final OrderedConfiguration<RequestFilter> configuration,
        final TypeCoercer typeCoercer, final WindowIdContext windowIdContext)
    {
        configuration.add("scopes", new RequestFilter()
        {
            public boolean service(Request request, Response response, RequestHandler handler) throws IOException
            {
                String windowIdString = request.getParameter("WINDOWID");

                Integer windowId = typeCoercer.coerce(windowIdString, Integer.class);

                windowIdContext.initWindowId(windowId);

                return handler.service(request, response);
            }
        });
    }

    @Contribute(ComponentEventLinkTransformer.class)
    public static void addLinkTransformer(OrderedConfiguration<ComponentEventLinkTransformer> configuration,
        final TypeCoercer typeCoercer, final WindowIdContext windowIdContext)
    {
        configuration.add("scopes", new ComponentEventLinkTransformer()
        {
            public Link transformComponentEventLink(Link defaultLink, ComponentEventRequestParameters parameters)
            {
                Integer windowId = windowIdContext.getWindowId();

                if (windowId != null)
                {
                    defaultLink.addParameter("WINDOWID", windowId.toString());
                }

                return defaultLink;
            }

            public ComponentEventRequestParameters decodeComponentEventRequest(Request request)
            {
                return null;
            }
        });
    }

    @Contribute(PageRenderLinkTransformer.class)
    public static void addPageLinkTransformer(OrderedConfiguration<PageRenderLinkTransformer> configuration,
        final TypeCoercer typeCoercer, final WindowIdContext windowIdContext)
    {
        configuration.add("scopes", new PageRenderLinkTransformer()
        {
            public Link transformPageRenderLink(Link defaultLink, PageRenderRequestParameters parameters)
            {
                Integer windowId = windowIdContext.getWindowId();

                if (windowId != null)
                {
                    defaultLink.addParameter("WINDOWID", windowId.toString());
                }

                return defaultLink;
            }

            public PageRenderRequestParameters decodePageRenderRequest(Request request)
            {
                return null;
            }
        });
    }

    public void contributeMarkupRenderer(OrderedConfiguration<MarkupRendererFilter> configuration,
        final AssetSource assetSource, final ThreadLocale threadLocale, final Environment environment,
        final Request request)
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
