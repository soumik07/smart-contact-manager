console.log("This is script file..")



const toggleSidebar = ()=>{
    if($(".sidebar").is(":visible")){   //sidebar is visible, we have to close it
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","2%");
    }
    else{                               						  //sidebar is closed, we have to open it 
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","22%");
    }
};