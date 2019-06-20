$(function(){
	//获得上下架状态，只要是在页面上获得值都是String类型
	var auditStatus = parseInt($("#auditStatus").val());
	if(auditStatus == 0){ 
		$("#label1").attr("class", "here");
	}else if(auditStatus == 1){
		$("#label3").attr("class", "here");
	}else if(auditStatus == 2){
		$("#label2").attr("class", "here");
	}else{
		$("#label4").attr("class", "here");
	}
	
	/* <span class="r page">
 	<!-- 变化后的pageNo -->
     <input type="hidden" id="pageNo" name="pageNo" />
     <!-- 两个隐藏域用于判断上一页和下一页的展示和隐藏 -->
     <input type="hidden" value="${page.pageNo}" id="currentPageNo" name="currentPageNo" />
     <input type="hidden" value="${page.totalPage}" id="totalPage" name="totalPage" />
             共<var id="pagePiece" class="orange">0</var>条<var id="pageTotal">1/1</var>
     <a href="javascript:void(0);" id="firstPage" class="hidden" >首页</a>
     <a href="javascript:void(0);" id="previous" class="hidden" title="上一页">上一页</a>
     <a href="javascript:void(0);" id="next" class="hidden" title="下一页">下一页</a>
     <a href="javascript:void(0);" id="lastPage" class="hidden">尾页</a>
 </span>*/
	
	//获得隐藏域的值
	var pageNo = parseInt($("#currentPageNo").val());
	var totalPage = parseInt($("#totalPage").val());
	/**
	 * 判断翻页按钮展示
	 */
	if(pageNo == 1 && pageNo == totalPage){
		$("#firstPage").hide();
		$("#lastPage").hide();
		$("#previous").hide();
		$("#next").hide();
	}else if(pageNo == 1 && totalPage > pageNo){
		$("#firstPage").hide();
		$("#lastPage").show();
		$("#previous").hide();
		$("#next").show();
	}else if(pageNo > 1 && totalPage > pageNo){
		$("#firstPage").show();
		$("#lastPage").show();
		$("#previous").show();
		$("#next").show();
	}else if(pageNo == totalPage && totalPage > 1){
		$("#firstPage").show();
		$("#lastPage").hide();
		$("#previous").show();
		$("#next").hide();
	}
	
	/**
	 * 点击下一页
	 */
	$("#next").click(function(){
		pageNo++;
		$("#pageNo").val(pageNo);
		$("#form1").submit();
	});
	$("#previous").click(function(){
		pageNo--;
		$("#pageNo").val(pageNo);
		$("#form1").submit();
	});
	$("#firstPage").click(function(){
		$("#pageNo").val(1);
		$("#form1").submit();
	});
	$("#lastPage").click(function(){
		$("#pageNo").val(totalPage);
		$("#form1").submit();
	});
	
	$("#selectPage").change(function(){
		var myPage = $(this).val();
		$("#pageNo").val(myPage);
		$("#form1").submit();
	});
	
	$("#selectPage").val(pageNo);
	
	
	$("#addItemNoteConfirm").click(function(){
		var notes = $("#itemNote").val();
		$("#notes").val(notes);
		//提交表单
		$("#auditForm").submit();
	});
})

function isPass(itemId, auditStatus){
	//把itemId和auditStatus给表单
	$("#itemId").val(itemId);
	$("#auditStatus1").val(auditStatus);
	
	$("#addItemNoteH2").html("商品审核");
	$("#itemNote").val("");
	tipShow("#addItemNote");
}