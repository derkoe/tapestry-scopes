package org.apache.tapestry.scopes.internal;

import org.apache.tapestry.scopes.WindowIdContext;
import org.apache.tapestry5.ioc.annotations.Scope;
import static org.apache.tapestry5.ioc.ScopeConstants.PERTHREAD;

@Scope(PERTHREAD)
public class WindowIdContextImpl implements WindowIdContext
{
    private Integer currentWindowId;

    public Integer getWindowId()
    {
        return currentWindowId;
    }

    public void initWindowId(Integer windowId)
    {
        if(currentWindowId == null)
        {
            this.currentWindowId = windowId;
        }
        else
        {
            throw new IllegalStateException("windowId is already set");
        }
    }
}
