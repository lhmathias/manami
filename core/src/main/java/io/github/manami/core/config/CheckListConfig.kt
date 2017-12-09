package io.github.manami.core.config

data class CheckListConfig(
        var checkLocations: Boolean,
        var checkCrc: Boolean,
        var checkMetaData: Boolean,
        var checkDeadEntries: Boolean
)