import android.content.Context
import android.content.SharedPreferences


class UserPreference(val context: Context) {
    companion object {

        lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences("MYAPP", Context.MODE_PRIVATE)
        }

        fun getString(key: String): String {
            return sharedPreferences.getString(key, "") ?: ""
        }

        fun setString(key: String, value: String?) {
            with(sharedPreferences.edit()) {
                putString(key, value)
                commit()
            }
        }

        fun getInt(key: String): Int {
            return sharedPreferences.getInt(key, 0)
        }

        fun setInt(key: String, value: Int) {
            with(sharedPreferences.edit()) {
                putInt(key, value)
                commit()
            }
        }

        fun getBoolean(key: String): Boolean {
            return sharedPreferences.getBoolean(key, false)
        }

        fun setBoolean(key: String, value: Boolean) {
            with(sharedPreferences.edit()) {
                putBoolean(key, value)
                commit()
            }
        }

        fun getLong(key: String): Long {
            return sharedPreferences.getLong(key, 0)
        }

        fun setLong(key: String, value: Long) {
            with(sharedPreferences.edit()) {
                putLong(key, value)
                commit()
            }
        }

        fun clearSharedPreference() {

            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }


    }
}