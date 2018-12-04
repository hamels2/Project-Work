<!doctype html>
<html>
<style>
.error {color: #FF0000;}
</style>

<?php

  $name=$email=$gender=$password=$confirm=$dob="";
  $nameErr=$emailErr=$genderErr=$passwordErr=$confirmErr=$dobErr="";
  $good=False;
  if($_SERVER["REQUEST_METHOD"]=="POST"){
    $good=True;
    if (empty($_POST["username"])) {
      $nameErr = "*Name is required";
      $good=False;
    }
    else{
      $name = validate($_POST["username"]);
    }

    if(empty($_POST["password"])){
      $passwordErr="*Password must be longer than 4 characters";
      $good=False;
    }
    else{
      $password=validate($_POST["password"]);
      if((strlen($password)<5)){
        $passwordErr="*Password must be longer than 4 characters";
        $good=False;
      }
    }
    if(empty($_POST["confirm"])){
      $good=False;
      $confirmErr="*Passwords do not match";
    }
      else{
        $confirm=validate($_POST["confirm"]);
        if($password!=$confirm){
          $confirmErr="*Passwords do not match";
          $good=False;
        }
    }
    if(empty($_POST["dob"])){
      $good=False;
      $dobErr="*Invalid date";
    }
    else{
      $dob=validate($_POST["dob"]);
      if(DateTime::createFromFormat('Y-m-d',$dob)==False){
        $dobErr="*Invalide date";
        $good=False;
      }
    }
    if(empty($_POST["email"])){
      $good=False;
      $emailErr="*Invalid Email";
    }
    else{
      $email=validate($_POST["email"]);
      if(!filter_var($email, FILTER_VALIDATE_EMAIL)){
        $emailErr="*Invalid email";
        $good=False;
      }
    }
    if(empty($_POST["gender"])){
      $good=False;
      $genderErr = "*Gender is required";
    }
    else{
      $gender=validate($_POST["gender"]);
    }

  }

  function validate($input){
    $data = trim($input);
    $data = htmlspecialchars($data);
    return $data;
  }


  if($good==True){
    $pdo = new PDO('mysql:host=localhost;dbname=courtside','root','4WW3');
    $stmt = $pdo->prepare('INSERT INTO users(name,password,dob,gender,email)
    VALUES (:name,:password,:dob,:gender,:email)');
    $stmt->bindValue(':name',$name);
    $stmt->bindValue(':password',$password);
    $stmt->bindValue(':dob',$dob);
    $stmt->bindValue(':gender',$gender);
    $stmt->bindValue(':email',$email);
    $stmt->execute();
    header('Location: login.php');
  }
?>
<meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- viewprt for mobile -->
  <head>
    <title>signup</title> <!--title of html doc -->
    <link href="signup.css" rel="stylesheet"/><!-- signup stylesheet used -->
  </head>


  <body>
    <header>
      <nav> <!--navigation bar-->
        <a href="login.html">Login</a>
        <a href="#">About</a> <!--not yet implemented so just reloads current page-->
      </nav>
    </header>


    <!--linebreak-->
    <form name="submit" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
      <p>Username:</p><br/>
      <input id=usr type="text" name="username" value="<?php echo $name;?>" ><!--username textbox-->
      <br/>
      <span class="error"><?php echo $nameErr;?></span>
      <br/>
      <p>email:</p><br/>
      <input id=usr type="text" name="email" value="<?php echo $email;?>" ><!--username textbox-->
      <br/>
      <span class="error"><?php echo $emailErr;?></span>
      <br/>
      <p>password:</p><br/>
      <input id=pass type="password" name="password" value="<?php echo $password;?>"> <!--password box-->
      <br/>
      <span class="error"><?php echo $passwordErr;?></span>
      <br/>
      <p>Reconfirm Password:</p><br/>
      <input id=conf type="password" name="confirm" value="<?php echo $confirm;?>"> <!--confirm password-->
      <br/>
      <span class="error"><?php echo $confirmErr;?></span>
      <br/>
      <input id=m type="radio" name="gender" <?php if (isset($gender) && $gender=="male") echo "checked";?> value="male">male <!--radio buttons for gender-->
      <input id=f type="radio" name="gender" <?php if (isset($gender) && $gender=="female") echo "checked";?> value="female">female
      <input id=n type="radio" name="gender" <?php if (isset($gender) && $gender=="non-binary") echo "checked";?> value="non">non binary
      <br/>
      <span class="error"><?php echo $genderErr;?></span>
      <br/>
      <p>Date of Birth:</p>
      <br/>
      <input id=dob type="date" value="<?php echo $dob;?>" name="dob"> <!--date of birth -->
      <span class="error"><?php echo $dobErr;?></span>
      <br/>
      <br/>
      <input type="submit" value = "Signup"> <!--signup button-->
    </form>
    <script src="Map.js"></script>
  </body>
  <footer>
    <p>Posted by: Sam Hamel</p>
    <p>Contact information: <a href="hamels2@mcmaster.ca">hamels2@mcmaster.ca</a>.</p>
  </footer>

</html>
