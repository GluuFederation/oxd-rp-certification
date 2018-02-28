function execRpScopeUserinfoClaimsTest1(url) {
    $.post("/gluu/tests/rp-scope-userinfo-claims", 
    	{},
    	function(data, status){
    	    alert("0.Data: " + data + "\nStatus: " + status);
    	    $.ajax({
    	    	method: "GET",
    	    	url: url	
    	    }).done(function(xmlHttp) {
    	    	console.log(xmlHttp.code);
    	    	if(xmlHttp.code == 303) {
    	    		window.location.href = "www.google.com"; 
    	    		//	alert("1.Data: " + data + "\nStatus: " + status);
    	    	}	
    	    });      	        
        }
    );
}

function execRpScopeUserinfoClaimsTest(url) {
    $.post("/gluu/tests/rp-scope-userinfo-claims", 
    	{},
    	function(data, status){
    		/*
    	    alert("0.Data: " + data + "\nStatus: " + status);
    	    $.ajax({
    	    	method: "GET",
    	    	url: url	
    	    }).done(function(xmlHttp) {
    	    	console.log(xmlHttp.code);
    	    	if(xmlHttp.code == 303) {
    	    		window.location.href = "www.google.com"; 
    	    		//	alert("1.Data: " + data + "\nStatus: " + status);
    	    	}	
    	    });
    	    */      	        
        }
    );
}