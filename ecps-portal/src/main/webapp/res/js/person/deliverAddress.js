$(function(){

	$("#province").change(function(){
		//获得选中的省id
		var areaId = $(this).val();
		loadOption(areaId, "mycity");
	});
	
	$("#mycity").change(function(){
		//获得选中的市id
		var areaId = $(this).val();
		loadOption(areaId, "district");
	});
	
	$("#jvForm").submit(function(){
		var isSubmit = true;
		var shipAddrId = $("#shipAddrId").val();
		if(shipAddrId == null || shipAddrId == ""){
			var addrLength = parseInt($("#addrLength").val());
			if(addrLength >= 5){
				isSubmit = false;
				alert("收货地址不能多于5个");
			}
		}
	
		return isSubmit;
	})
	
	
});

function loadOption(areaId, selectId){
	
	$.ajax({
		url:path+"/user/login/getChildArea.do",
		type:"post",
		dataType:"text",
		async:false,
		data:{
			areaId:areaId
		},
		success:function(responseText){
			//alert(responseText);
			var jsonObj = $.parseJSON(responseText);
			var aList = jsonObj.areaList;
			
			
			if(selectId == "mycity"){
				$("#mycity").empty();
				$("#district").empty();
				$("#mycity").append("<option value='-1'>城市</option>");
				$("#district").append("<option value='-1'>县/区</option>");
			}else{
				$("#district").empty();
				$("#"+selectId).append("<option value=''>县/区</option>");
			}
			if(aList != null && aList.length > 0){
				var option = "";
				//数组不能用each不来遍历
				for(var i = 0; i < aList.length; i++){
					option = option + "<option value='"+aList[i].areaId+"'>"+aList[i].areaName+"</option>";
				}
				$("#"+selectId).append(option);
				
			}
			
		}
	})
}

function modify(shipAddrId){
	$.ajax({
		url:path+"/user/login/getAddrById.do",
		type:"post",
		dataType:"text",
		data:{
			shipAddrId:shipAddrId
		},
		success:function(responseText){
			var jsonObj = $.parseJSON(responseText);
			$("#shipAddrId").val(jsonObj.addr.shipAddrId);
			$("#shipName").val(jsonObj.addr.shipName);
			$("#province").val(jsonObj.addr.province);
			//加载城市的option
			loadOption(jsonObj.addr.province, "mycity");
			$("#mycity").val(jsonObj.addr.city);
			loadOption(jsonObj.addr.city, "district");
			$("#district").val(jsonObj.addr.district);
			$("#addr").val(jsonObj.addr.addr);
			$("#zipCode").val(jsonObj.addr.zipCode);
			$("#phone").val(jsonObj.addr.phone);
			if(jsonObj.addr.defaultAddr == 1){
				$("#defaultAddr").attr("checked", "checked");
			}else{
				$("#defaultAddr").removeAttr("checked");
			}
		}
	})
}
