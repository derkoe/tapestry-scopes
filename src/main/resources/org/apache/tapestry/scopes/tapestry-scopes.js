/**
 * Get URL parameters
 * @param the name of the parameter from the URL to retrieve
 * @return the requested parameter value if exists else an empty string
 */
Tapestry.getUrlParam = function(name)
{ 
    var regexS;
    var regexl;
    var results;
 
    name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
    regexS = "[\\?&]"+name+"=([^&#]*)";
    regex = new RegExp(regexS);
    results = regex.exec (window.location.href);
            //note: don't write space after command exec
 
    if ( results == null ) {
        return "";
    } else {
        return results[1];
    }
}

Tapestry.Initializer.scopesInit = function(path) 
{
	var windowName = Tapestry.getUrlParam("WINDOWID");

	if (!window.name || window.name.length < 3 || windowName != window.name)
	{	
		alert("New window / changed window name");

		var now = new Date();

		window.name = now.getHours() + now.getMilliseconds();

		if (window.location.href.search("WINDOWID") >= 0)
		{
			alert("A: " + window.name);
			window.location.href = window.location.href.replace(/WINDOWID=[^&]*/, 'WINDOWID=' + window.name)
		}
		else
		{
			alert("B");
			window.location.href = window.location.href + "?WINDOWID=" + window.name
		}

		return;
	}
}
