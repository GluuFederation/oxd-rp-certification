function execRpResponseTypeCodeTest(url) {
    $.post("/gluu/tests/rp_response_type_code", 
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