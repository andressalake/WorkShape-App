var express = require('express');
var router = express.Router();

/* GET users listing. */
// router.get('/', function(req, res) {
//   res.send('respond with a resource');
// });

router.post('/', function (req, res) {
	// console.log(');

	console.log(req.body.username);
	console.log(req.body.password);

	console.log(JSON.stringify(req.body));

	// console.log(req.body.hasOwnProperty('username'));

	// res.send(JSON.stringify({status:"created"}));

	// ---------------------------------------------------------
	// Add in password verification
	// ---------------------------------------------------------
	
	if (req.body.hasOwnProperty("password")) {
		res.send({"sessionID":"fdsahjkfdlsahjfkldas",
				  "expires":"1400495563"});
	} else {
		res.status(401);
		res.send({"status":"Not Authorized", "reason":"Bad Password"});
	}
});

module.exports = router;
