package io.github.manamiproject.manami.core.tasks.checklist

internal interface Checker {

   /**
    * @return The number of work items which will be processed.
    */
   fun estimateWorkload(): Int

   fun check()
}