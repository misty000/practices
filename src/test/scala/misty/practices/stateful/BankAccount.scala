package misty.practices.stateful

/**
 * Created with IntelliJ IDEA.
 * User: Misty
 * Date: 13-3-9
 * Time: 下午4:22
 */
class BankAccount {
	private var bal: Int = 0

	def balance: Int = bal

	def deposit(amount: Int) {
		require(amount > 0)
		bal += amount
	}

	def withdraw(amount: Int): Boolean = {
		if (amount > bal) false
		else {
			bal -= amount
			true
		}
	}
}
