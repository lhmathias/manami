package io.github.manamiproject.manami.core.tasks.checklist

/**
 * Perform a specific check on an anime list.
 */
internal interface Checker {

   /**
    * @return The number of work-items which will be processed.
    */
   fun estimateWorkload(): Int

   /**
    * Perform check on anime list.
    */
   fun check()
}