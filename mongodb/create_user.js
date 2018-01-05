// create user
var mongo = db.getMongo()
var users = ["a", "b","c"]
var dbNames = ["db1", "db2"]
var password = "password"

dbNames.forEach(dbName => {
	db = mongo.getDB(dbName);
	users.forEach(userName => {
		if (db.getUser(userName)) {
			db.dropUser(userName)
		}
		db.createUser(
			{
				user: userName,
				pwd: password,
				roles: [
					{
						role: "dbOwner",
						db: dbName
					}
				]
			}
		);
	})
})
