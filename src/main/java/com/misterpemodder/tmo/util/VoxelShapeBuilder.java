package com.misterpemodder.tmo.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class VoxelShapeBuilder {
  private List<Cuboid> coboids;
  private float xRotation;
  private float yRotation;
  private float zRotation;
  private Vec3d translation;
  private Vec3d origin;

  public VoxelShapeBuilder() {
    this.coboids = new ArrayList<>();
    this.xRotation = 0.0F;
    this.yRotation = 0.0F;
    this.zRotation = 0.0F;
    this.translation = new Vec3d(0.0, 0.0, 0.0);
    this.origin = new Vec3d(0.0, 0.0, 0.0);
  }

  public VoxelShapeBuilder origin(double x, double y, double z) {
    this.origin = new Vec3d(x, y, z);
    return this;
  }

  public VoxelShapeBuilder translate(double x, double y, double z) {
    this.translation = new Vec3d(x, y, z);
    return this;
  }

  public VoxelShapeBuilder add(double x, double y, double z, double width, double height,
      double depth) {
    this.coboids.add(new Cuboid(x, y, z, width, height, depth));
    return this;
  }

  public VoxelShapeBuilder rotateX(float rotation) {
    this.xRotation = (float) (rotation * Math.PI / 180.0);
    return this;
  }

  public VoxelShapeBuilder rotateY(float rotation) {
    this.yRotation = (float) (rotation * Math.PI / 180.0);
    return this;
  }

  public VoxelShapeBuilder rotateZ(float rotation) {
    this.zRotation = (float) (rotation * Math.PI / 180.0);
    return this;
  }

  public VoxelShapeBuilder rotation(float xRotation, float yRotation, float zRotation) {
    this.xRotation = (float) (xRotation * Math.PI / 180.0);
    this.yRotation = (float) (yRotation * Math.PI / 180.0);
    this.zRotation = (float) (zRotation * Math.PI / 180.0);
    return this;
  }

  public VoxelShape build() {
    VoxelShape ret = this.coboids.stream()
        .map(c -> c.createShape(this.xRotation, this.yRotation, this.zRotation, this.translation,
            this.origin))
        .reduce(VoxelShapes.empty(),
            (f, s) -> VoxelShapes.combineAndSimplify(f, s, BooleanBiFunction.OR));
    this.xRotation = 0.0F;
    this.yRotation = 0.0F;
    this.zRotation = 0.0F;
    return ret;
  }

  private static class Cuboid {
    private double xMin;
    private double yMin;
    private double zMin;
    private double xMax;
    private double yMax;
    private double zMax;

    public Cuboid(double x, double y, double z, double width, double height, double depth) {
      this.xMin = x;
      this.yMin = y;
      this.zMin = z;
      this.xMax = x + width;
      this.yMax = y + height;
      this.zMax = z + depth;
    }

    public VoxelShape createShape(float xRotation, float yRotation, float zRotation,
        Vec3d translation, Vec3d origin) {
      Vertex min = new Vertex(this.xMin, this.yMin, this.zMin);
      Vertex max = new Vertex(this.xMax, this.yMax, this.zMax);
      min.x -= origin.x;
      min.y -= origin.y;
      min.z -= origin.z;
      max.x -= origin.x;
      max.y -= origin.y;
      max.z -= origin.z;
      min.rotateX(xRotation);
      min.rotateY(yRotation);
      min.rotateZ(zRotation);
      max.rotateX(xRotation);
      max.rotateY(yRotation);
      max.rotateZ(zRotation);
      min.x += origin.x;
      min.y += origin.y;
      min.z += origin.z;
      max.x += origin.x;
      max.y += origin.y;
      max.z += origin.z;
      return Block.createCuboidShape(Math.min(min.x, max.x) + translation.x,
          Math.min(min.y, max.y) + translation.y, Math.min(min.z, max.z) + translation.z,
          Math.max(min.x, max.x) + translation.x, Math.max(min.y, max.y) + translation.y,
          Math.max(min.z, max.z) + translation.z);
    }
  }

  private static class Vertex {
    public double x;
    public double y;
    public double z;

    public Vertex(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public void rotateX(float angle) {
      float cosX = MathHelper.cos(angle);
      float sinX = MathHelper.sin(angle);
      double ny = this.y;

      this.y = this.y * cosX + this.z * sinX;
      this.z = this.z * cosX - ny * sinX;
    }

    public void rotateY(float angle) {
      float cosY = MathHelper.cos(angle);
      float sinY = MathHelper.sin(angle);
      double nx = this.x;

      this.x = this.x * cosY + this.z * sinY;
      this.z = this.z * cosY - nx * sinY;
    }

    public void rotateZ(float angle) {
      float cosZ = MathHelper.cos(angle);
      float sinZ = MathHelper.sin(angle);
      double nx = this.x;

      this.x = this.x * cosZ + this.y * sinZ;
      this.y = this.y * cosZ - nx * sinZ;
    }
  }
}
