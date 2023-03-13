package data

import data.mockObject.FakeDialog

private fun getRandomLong() = (Math.random() * 1000).toLong()
fun getFakeDialogs() = listOf(
    FakeDialog("1", "Mr. Smithajfhkalsgodhjgkasgandfhjka[sgnjfhsdjhkasgha8ys8dfihJabvusighjabsiasujganbfklnmvadifgijarguajmKBNaisu", getRandomLong(), getRandomLong()),
    FakeDialog("2", "Mr. Williams", getRandomLong(), getRandomLong()),
    FakeDialog("3", "Mr. Peters", getRandomLong(), getRandomLong()),
    FakeDialog("4", "Mr. Gibson", getRandomLong(), getRandomLong()),
    FakeDialog("5", "Mr. Martin", getRandomLong(), getRandomLong()),
    FakeDialog("6", "Mr. Jordan", getRandomLong(), getRandomLong()),
    FakeDialog("7", "Mr. Evans", getRandomLong(), getRandomLong()),
    FakeDialog("8", "Mr. Roberts", getRandomLong(), getRandomLong()),
    FakeDialog("9", "Mr. Lewis", getRandomLong(), getRandomLong()),
    FakeDialog("10", "Mrs. Stone", getRandomLong(), getRandomLong()),
    FakeDialog("11", "Mrs. Bell", getRandomLong(), getRandomLong()),
    FakeDialog("12", "Mrs. Campbell", getRandomLong(), getRandomLong()),
    FakeDialog("13", "Mrs. Florence", getRandomLong(), getRandomLong()),
    FakeDialog("14", "Mrs. Adams", getRandomLong(), getRandomLong()),
    FakeDialog("15", "Mrs. Lewis", getRandomLong(), getRandomLong()),
    FakeDialog("16", "Mrs. Mills", getRandomLong(), getRandomLong()),
    FakeDialog("17", "Mrs. Grant", getRandomLong(), getRandomLong()),
    FakeDialog("18", "Mrs. Evans", getRandomLong(), getRandomLong()),
    FakeDialog("19", "Mrs. Gibson", getRandomLong(), getRandomLong()),
    FakeDialog("20", "Mrs. Smith", getRandomLong(), getRandomLong())
)
