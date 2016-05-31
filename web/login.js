$("#sub").click(function(){
    $.ajax({
        cache: true,
        type: "POST",
        url:"UserLoginAction",
        data:$("#loginform").serialize(),
        async: false
    });
    return true;

});