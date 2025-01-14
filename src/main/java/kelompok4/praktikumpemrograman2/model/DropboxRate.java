package kelompok4.praktikumpemrograman2.model;

import java.math.BigDecimal;

public class DropboxRate {
    private int id;
    private int dropboxId;
    private BigDecimal minDistance;
    private BigDecimal maxDistance;
    private BigDecimal baseRate;
    private BigDecimal distanceRate;
    private LokasiDropbox dropbox;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDropboxId() { return dropboxId; }
    public void setDropboxId(int dropboxId) { this.dropboxId = dropboxId; }

    public BigDecimal getMinDistance() { return minDistance; }
    public void setMinDistance(BigDecimal minDistance) { this.minDistance = minDistance; }

    public BigDecimal getMaxDistance() { return maxDistance; }
    public void setMaxDistance(BigDecimal maxDistance) { this.maxDistance = maxDistance; }

    public BigDecimal getBaseRate() { return baseRate; }
    public void setBaseRate(BigDecimal baseRate) { this.baseRate = baseRate; }

    public BigDecimal getDistanceRate() { return distanceRate; }
    public void setDistanceRate(BigDecimal distanceRate) { this.distanceRate = distanceRate; }

    public LokasiDropbox getDropbox() { return dropbox; }
    public void setDropbox(LokasiDropbox dropbox) { this.dropbox = dropbox; }
}