package com.example.xfund.data

import com.example.xfund.R
import com.example.xfund.model.CommunityDiscussion

class CommunityDatasource {
    fun loadCommunities(): List<CommunityDiscussion> {
        return listOf<CommunityDiscussion>(
            CommunityDiscussion(R.string.affirmation1),
            CommunityDiscussion(R.string.affirmation2),
            CommunityDiscussion(R.string.affirmation3),
            CommunityDiscussion(R.string.affirmation4),
            CommunityDiscussion(R.string.affirmation5),
            CommunityDiscussion(R.string.affirmation6),
            CommunityDiscussion(R.string.affirmation7),
            CommunityDiscussion(R.string.affirmation8),
            CommunityDiscussion(R.string.affirmation9),
            CommunityDiscussion(R.string.affirmation10)
        )
    }
}