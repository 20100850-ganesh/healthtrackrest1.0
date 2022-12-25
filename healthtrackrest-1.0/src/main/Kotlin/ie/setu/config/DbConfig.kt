package ie.setu.config

import org.jetbrains.exposed.sql.Database

class DbConfig{

    //NOTE: you need the ?sslmode=require otherwise you get an error complaining about the ssl certificate
    fun getDbConnection() :Database{
        return Database.connect(
            "jdbc:postgresql://ec2-44-194-92-192.compute-1.amazonaws.com:5432/d90h7tf27ks2p3?sslmode=require",
            driver = "org.postgresql.Driver",
            user = "qozwswwyzscdaw",
            password = "c34ce9ff80e61a14590e3ab52a88d7a174761ce5e95f2e503d49731c57694a41")
    }

}