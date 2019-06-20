$(function(){
	$.ajax({
		url:path+"/user/getUser.do",
		type:"post",
		dataType:"text",
		success:function(responseText){
			//alert(responseText);
			var jsonObj = $.parseJSON(responseText);
			if(jsonObj.user != null){
				$("#loginAlertIs").html(jsonObj.user.username);
			}
		}
	})
})