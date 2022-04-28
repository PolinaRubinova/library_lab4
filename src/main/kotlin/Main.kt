import Interaction.CachingInteraction
import Interaction.NonCachingInteraction

fun main() {
    val iterations = 5000

    for (interaction in (listOf(NonCachingInteraction, CachingInteraction))) {
        val testThreads = listOf(
            GetAllGenresThread(iterations, interaction),
            GetBookThread(iterations, interaction),
            AddAuthorThread(iterations, interaction),
            AddBookThread(iterations, interaction),
            ChangeStaffPhoneNumberThread(iterations, interaction),
            ChangeReaderDateReturnThread(iterations, interaction),
            DeletePublisherThread(iterations, interaction)
        )

        testThreads.forEach { it.start() }
        testThreads.forEach { it.join() }

        println("\n" + interaction::class.java + "\n\nAverage")
        for (t in testThreads) {
            println("${t::class.java} ${t.iterations} - ${(t.times.sum() / t.iterations) / 1000000f}")
        }
        println("\nMax")
        for (t in testThreads) {
            println("${t::class.java} ${t.iterations} - ${t.times.maxByOrNull { it }!! / 1000000f}")
        }
        println("\nMin")
        for (t in testThreads) {
            println("${t::class.java} ${t.iterations} - ${t.times.minByOrNull { it }!! / 1000000f}")
        }
    }
}