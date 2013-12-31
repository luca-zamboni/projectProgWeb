$(document).ready(function(){
    
    $("#regBtn").click(function(){
        form = $("#regForm");
        email = form.find("input[name='mail']");
        user  = form.find("input[name='username']");
        pass  = form.find("input[name='passwd']");
        
        isValidEmail=validateEmail(email.val());
        
        if(user.val().length<1 && isValidEmail)
            user.val(""+email.val());
        
        isValidUsername=validateText(user.val());
        isValidPass=validateText(pass.val());
        
        if(!isValidEmail){
            email.effect("shake");
        }
        if(!isValidUsername){
            user.effect("shake");
        }
        if(!isValidPass){
            pass.effect("shake");
        }
        
        if(isValidEmail&&isValidPass&&isValidUsername){
            params="mail="+email.val()+"&username="+user.val()+"&passwd="+pass.val();
            $.post("register",params,function(data){document.write(data)});
        }
    });
    
    var validateEmail = function(mail){
         var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
         return filter.test(mail);
    }
    var validateText = function(txt){
        return txt.length>5;
    }
});

