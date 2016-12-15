package org.devfleet.crest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CrestAccess {
    // Missing masks
    // characterFittingsRead
    //characterLocationRead
    //characterLoyaltyPointsRead
    ///characterOpportunitiesRead
    ///characterStatsRead
    //fleetread
    //structureVulnUpdate

    public static final Map<String, Long> MASKS;
    static {
        final Map<String, Long> m = new HashMap<>();
        m.put("characterAccountRead", 33554432l);
        m.put("characterStatsRead", 16777216l);
        m.put("characterWalletRead", 1l | 2097152l | 4194304l);
        m.put("characterAssetsRead", 2l | 134217728l);
        m.put("characterCalendarRead", 4l | 1048576l);
        m.put("characterContactsRead", 16l | 32l | 524288l);
        m.put("characterFactionalWarfareRead", 64l);
        m.put("characterIndustryJobsRead", 128l);
        m.put("characterKillsRead", 256l);
        m.put("characterMailRead", 512l | 1024l | 2048l);
        m.put("characterMarketOrdersRead", 4096l);
        m.put("characterMedalsRead", 8192l);
        m.put("characterNotificationsRead", 16384l | 32768l);
        m.put("characterResearchRead", 65536l);
        m.put("characterSkillsRead", 131072l | 262144l | 1073741824l);
        m.put("characterAccountRead", 33554432l);
        m.put("characterContractsRead", 67108864l);
        m.put("characterBookmarksRead", 268435456l);
        m.put("characterChatChannelsRead", 536870912l);
        m.put("characterClonesRead", 2147483648l);

        m.put("corporationWalletRead", 1l | 8l | 1048576l | 2097152l);
        m.put("corporationAssetsRead", 2l | 32l | 16777216l);
        m.put("corporationMedalsRead", 4l | 8192l);
        m.put("corporationContactsRead", 16l | 262144l);
        m.put("corporationFactionalWarfareRead", 64l);
        m.put("corporationIndustryJobsRead", 128l);
        m.put("corporationKillsRead", 256l);
        m.put("corporationMembersRead", 512l | 1024l | 2048l | 4194304l | 33554432l);
        m.put("corporationMarketOrdersRead", 4096l);
        m.put("corporationStructuresRead", 16384l | 32768l | 131072l);
        m.put("corporationShareholdersRead", 65536l);
        m.put("corporationContractsRead", 8388608l);
        m.put("corporationBookmarksRead", 67108864l);

        MASKS = Collections.unmodifiableMap(m);
    };

    public static final Map<String, String> DESCRIPTIONS;
    static {
        final Map<String, String> m = new HashMap<>();
        m.put("publicData", "Publicly available information.");
        m.put("characterAccountRead", "Read your character's account information.");
        m.put("characterAssetsRead", "Read your character's assets.");
        m.put("characterBookmarksRead", "Read your character's bookmarks.");
        m.put("characterCalendarRead", "Read your character's calendar.");
        m.put("characterChatChannelsRead", "Read a list of the chat channels your character is in.");
        m.put("characterClonesRead", "Read a list of your character's clones.");
        m.put("characterContactsRead", "Read your character's contacts.");
        m.put("characterContactsWrite", "Create and update your character's contacts.");
        m.put("characterFactionalWarfareRead", "Read your character's FW information.");
        m.put("characterFittingRead", "Read all of the saved fits for a character.");
        m.put("characterFittingWrite", "Create new saved fits as well as delete existing ones.");
        m.put("characterIndustryJobsRead", "Read your character's industry jobs.");
        m.put("characterKillsRead", "Read your character's killmails. Only killmail that the character is the victim or the finalBlow will be returned with this scope.");
        m.put("characterLocationRead", "Read your character's current location.");
        m.put("characterMailRead", "Read your character's mail.");
        m.put("characterMarketOrdersRead", "Read your character's market orders.");
        m.put("characterMedalsRead", "Read your character's medals.");
        m.put("characterNavigationWrite", "Set your auto-pilot waypoints on your client.");
        m.put("characterNotificationsRead", "Read your character's notifications.");
        m.put("characterResearchRead", "Read your character's research information.");
        m.put("characterSkillsRead", "Read your character's skills.");
        m.put("characterWalletRead", "Read your character's wallet.");
        m.put("fleetRead", "");
        m.put("fleetWrite", "");
        m.put("structureVulnUpdate", "");
        m.put("corporationWalletRead", "Read your corporation's wallet.");
        m.put("corporationAssetsRead", "Read your corporation's assets.");
        m.put("corporationMedalsRead", "Read your corporation's medals list.");
        m.put("corporationContactsRead", "Read your corporation's contacts.");
        m.put("corporationFactionalWarfareRead", "Read your corporation's FW information.");
        m.put("corporationIndustryJobsRead", "Read your corporation's industry jobs.");
        m.put("corporationKillsRead", "Read your corporation's killmails.");
        m.put("corporationMembersRead", "Read your corporation's members list.");
        m.put("corporationMarketOrdersRead", "Read your corporation's market orders.");
        m.put("corporationStructuresRead", "Read your corporation's structures information.");
        m.put("corporationShareholdersRead","Read your corporation's shareholders information.");
        m.put("corporationContractsRead", "Read your corporation's contracts.");
        m.put("corporationBookmarksRead", "Read your corporation's bookmarks.");

        DESCRIPTIONS = Collections.unmodifiableMap(m);
    }

    public static final String[] PUBLIC_SCOPES = {
            "publicData"
    };

    public static final String[] CHARACTER_SCOPES = {
            "characterAccountRead",
            "characterAssetsRead",
            "characterBookmarksRead",
            "characterCalendarRead",
            "characterChatChannelsRead",
            "characterClonesRead",
            "characterContactsRead",
            "characterContactsWrite",
            "characterContractsRead",
            "characterFactionalWarfareRead",
            "characterFittingsRead",
            "characterFittingsWrite",
            "characterIndustryJobsRead",
            "characterKillsRead",
            "characterLocationRead",
            "characterLoyaltyPointsRead",
            "characterMailRead",
            "characterMarketOrdersRead",
            "characterMedalsRead",
            "characterNavigationWrite",
            "characterNotificationsRead",
            "characterOpportunitiesRead",
            "characterResearchRead",
            "characterSkillsRead",
            "characterStatsRead",
            "characterWalletRead",
            "fleetRead",
            "fleetWrite",
            "structureVulnUpdate"
    };

    public static final String[] CORPORATION_SCOPES = {
            "corporationAssetRead",
            "corporationBookmarksRead",
            "corporationContractsRead",
            "corporationFactionalWarfareRead",
            "corporationIndustryJobsRead",
            "corporationKillsRead",
            "corporationMarketOrdersRead",
            "corporationMedalsRead",
            "corporationMembersRead",
            "corporationShareholdersRead",
            "corporationStructuresRead",
            "corporationWalletRead",
            "structureVulnUpdate"
    };

    private CrestAccess() {}

    public static long getAccessMask(final String... scopes) {
        long m = 0;
        for (String s: scopes) {
            Long v = MASKS.get(s);
            if (null != v) {
                m = m | v;
            }
        }
        return m;
    }

    public static List<String> getScope(final long accessMask) {
        final List<String> scopes = new ArrayList<>();
        for (Map.Entry<String, Long> m: CrestAccess.MASKS.entrySet()) {
            if ((m.getValue() & accessMask) == m.getValue()) {
                scopes.add(m.getKey());
            }
        }
        return scopes;
    }

    public static boolean hasAnyScope(final String[] scopes, final long mask) {
        for (String s: scopes) {
            Long value = MASKS.get(s);
            if ((null != value) && ((value & mask) == value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAllScopes(final String[] scopes, final long mask) {
        for (String s: scopes) {
            Long value = MASKS.get(s);
            if (null == value) {
                return false;
            }
            if ((value & mask) != value) {
                return false;
            }
        }
        return true;
    }
}
