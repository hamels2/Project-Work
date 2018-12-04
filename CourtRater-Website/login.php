<!doctype html>
<html>
<style>
.error {color: #FF0000;}
</style>

<meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- viewprt for mobile -->
  <head>
    <title>login</title> <!--title of html doc -->
    <link href="login.css" rel="stylesheet"/> <!-- login stylesheet used -->
  </head>
  <?php
    $emailErr=$passwordErr=$password=$email="";
    if($_SERVER["REQUEST_METHOD"]=="POST"){
      $password = $_POST['password'];
      $email = $_POST['email'];
      $chk = new PDO('mysql:host=localhost;dbname=courtside','root','4WW3');
      $stmt=$chk->prepare('SELECT email, password FROM `users` WHERE `email` LIKE :email');
      $stmt->bindValue(':email',$email);
      $stmt->execute();
      $result = $stmt->fetch(PDO::FETCH_ASSOC);
      if($result['email']!=$email){
        $emailErr="*Email does not exist";
      }
      else if($result['password']!=$password){
        $passwordErr="*Invalid Password";
      }
      else{
        session_start();
        $_SESSION['email']=$email;
        header('Location: search.php');
      }
    }
  ?>



  <body>
    <header>
      <nav>
        <a href="Signup.html">Signup</a>
        <a href="#">About</a> <!--not yet implemented so just reloads current page-->
      </nav>
    </header>


    <div>
      <h1 class="wel">Welcome to Courtside!</h1>
      <h2>Login</h2>
      <form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> <!--form sends to search-->
        Email:<br/>
        <input type="text" name="email" value="<?php echo $email;?>" autofocus> <!--username textbox-->
        <br/>
        <span class="error"><?php echo $emailErr;?></span>
        <br/>
        password:<br/>
        <input type="password" name="password" value="<?php echo $password;?>"> <!--password box-->
        <br/>
        <span class="error"><?php echo $passwordErr;?></span>
        <br/>

        <input type="submit" value = "Login">
      </form>
    </div>

    <!-- <p> p is for paragraph</p> -->
  </body>
  <footer>
    <p>Posted by: Sam Hamel</p>
    <p>Contact information: <a href="hamels2@mcmaster.ca">hamels2@mcmaster.ca</a>.</p>
  </footer>
</html>
