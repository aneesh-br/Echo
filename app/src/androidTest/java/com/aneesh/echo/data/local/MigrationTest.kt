package com.aneesh.echo.data.local

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {
    private val TEST_DB = "migration-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        EchoDatabase::class.java
    )

    @Test
    fun migrate1To2_keepsStoryAndAddsCommentCountDefault0() {
        // Build a REAL v1 database from 1.json, insert the old-shaped row, close it.
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL("INSERT INTO stories (id, title, author, score) VALUES (123, 'Hello', 'pg', 10)")
            close()
        }

        // Run MIGRATION_1_2 and validate against 2.json (Expected vs Found).
        val db = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        // Read the row back and assert 3 things:
        db.query("SELECT * FROM stories WHERE id = 123").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("Hello", cursor.getString(cursor.getColumnIndexOrThrow("title")))
            assertEquals(0, cursor.getInt(cursor.getColumnIndexOrThrow("commentCount")))
        }
    }
}