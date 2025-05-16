package com.example.team7_realhelper;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class UsageStatsHelper {

    private Context context;

    public UsageStatsHelper(Context context) {
        this.context = context;
    }

    public boolean hasUsageStatsPermission() {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*60, time);
        return stats != null && !stats.isEmpty();
    }

    public String getForegroundAppPackageName() {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();

        // 간격 줄임: 최근 3초
        List<UsageStats> usageStatsList = usm.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - 1000 * 3,
                time
        );

        if (usageStatsList == null || usageStatsList.isEmpty()) {
            return "unknown";
        }

        SortedMap<Long, UsageStats> sortedMap = new TreeMap<>();
        for (UsageStats usageStats : usageStatsList) {
            sortedMap.put(usageStats.getLastTimeUsed(), usageStats);
        }

        if (!sortedMap.isEmpty()) {
            return sortedMap.get(sortedMap.lastKey()).getPackageName();
        }

        return "unknown";
    }

}
