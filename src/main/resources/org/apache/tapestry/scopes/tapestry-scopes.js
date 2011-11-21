Tapestry.Initializer.scopesInit = function(path) 
{
	alert(window.name);
	if (!window.name || window.name.length < 3)
	{
		window.name = 'hallo';	
	}
}
