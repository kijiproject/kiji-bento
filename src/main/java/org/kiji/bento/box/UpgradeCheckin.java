/**
 * (c) Copyright 2013 WibiData, Inc.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kiji.bento.box;

import static com.google.common.base.Preconditions.*;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <p>Represents a check-in message that can be sent to a Kiji BentoBox upgrade server. A check-in
 * message contains information such as the version of Kiji BentoBox being used,
 * a unique identifier for the user of Kiji BentoBox, and the last time the <code>kiji</code>
 * script included with the distribution was used. Check-in messages are periodically sent to the
 * upgrade server both so it can track usage information and as a means of requesting information
 * on new, available updates.
 *
 * <p>And now I explain how you get one of these things and how you transform it into JSON.
 */
public final class UpgradeCheckin {

  /** The type of this message (used by the upgrade server). */
  @SerializedName("type")
  private final String mType;

  /** The format version of this message. */
  @SerializedName("request_format")
  private final String mFormat;

  /** The operating system of the current user sending this check-in message. */
  @SerializedName("os")
  private final String mOperatingSystem;

  /** The version of BentoBox being used by the user sending this check-in message. */
  @SerializedName("bento_version")
  private final String mBentoVersion;

  /** The Java version being used by the current user sending this check-in message. */
  @SerializedName("java_version")
  private final String mJavaVersion;

  /**
   * Unix time in milliseconds of the last usage of the <code>kiji</code> command by the user
   * sending this check-in message.
   */
  @SerializedName("last_used")
  private final long mLastUsedMillis;

  /** A unique (anonymous) identifer for the user sending this check-in message. */
  @SerializedName("id")
  private final String mId;

  /**
   * Constructs a new check-in message that will contain the specified values. Clients cannot use
   * this constructor and should instead use a {@link Builder} to create a check-in message.
   *
   * @param type of message being sent.
   * @param format contains a version for the message format itself.
   * @param operatingSystem in use by the client sending the message.
   * @param bentoVersion in use by the client sending the message.
   * @param javaVersion in use by the client sending the message.
   * @param lastUsedMillis is the unix time, in milliseconds, of the last usage of the
   *     <code>kiji</code> script included with BentoBox.
   * @param id that is unique and anonymous for the client sending the message.
   */
  private UpgradeCheckin(String type, String format, String operatingSystem,
      String bentoVersion, String javaVersion, long lastUsedMillis, String id) {
    mType = type;
    mFormat = format;
    mOperatingSystem = operatingSystem;
    mBentoVersion = bentoVersion;
    mJavaVersion = javaVersion;
    mLastUsedMillis = lastUsedMillis;
    mId = id;
  }

  /**
   * Creates a JSON serialization of this check-in message.
   *
   * @return a JSON string that is a serialization of this instance.
   */
  public String toJSON() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof UpgradeCheckin)) {
      return false;
    }
    UpgradeCheckin that = (UpgradeCheckin) other;
    return mType.equals(that.getType())
        && mFormat.equals(that.getFormat())
        && mOperatingSystem.equals(that.getOperatingSystem())
        && mBentoVersion.equals(that.getBentoVersion())
        && mJavaVersion.equals(that.getJavaVersion())
        && mLastUsedMillis == that.getLastUsedMillis()
        && mId.equals(that.getId());
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(mType)
        .append(mFormat)
        .append(mOperatingSystem)
        .append(mBentoVersion)
        .append(mJavaVersion)
        .append(mLastUsedMillis)
        .append(mId)
        .toHashCode();
  }

  /**
   * @return the type of this message.
   */
  public String getType() {
    return mType;
  }

  /**
   * @return the format version for this message.
   */
  public String getFormat() {
    return mFormat;
  }

  /**
   * @return the operating system information included with this message.
   */
  public String getOperatingSystem() {
    return mOperatingSystem;
  }

  /**
   * @return the BentoBox version included with this message.
   */
  public String getBentoVersion() {
    return mBentoVersion;
  }

  /**
   * @return the Java version included with this message.
   */
  public String getJavaVersion() {
    return mJavaVersion;
  }

  /**
   * @return the Unix time in milliseconds of the last usage of the <code>kiji</code> script
   *     included with this message.
   */
  public long getLastUsedMillis() {
    return mLastUsedMillis;
  }

  /**
   * @return the user identifier included with this message.
   */
  public String getId() {
    return mId;
  }

  /**
   * <p>Can be used to build an upgrade check-in message. Clients should use the "with"
   * methods of this builder to configure message parameters, and then call {@link #build()} to
   * obtain an instance of {@link UpgradeCheckin} with content equivalent to the values
   * configured. It is an error to attempt to obtain an instance with {@link #build()} without
   * first supplying all content for the messsage.
   *
   * <p>This builder will only create check-in messages with type <code>checkversion</code> and
   * format <code>bento-checkin-1.0.0</code>. Furthermore, operating system, bento version,
   * and java version information included in the check-in message will be set by this builder
   * using {@link System#getProperties()}. Clients should only need to set the user id and last
   * usage timestamp of the message through the builder to create a new check-in message.
   */
  public static class Builder {
    /** The type of message created by this builder. */
    private static final String MESSAGE_TYPE = "checkversion";

    /** The format of messages created by this builder. */
    private static final String MESSAGE_FORMAT = "bento-checkin-1.0.0";

    /**
     * The usage timestamp for the <code>kiji</code> command to include in the built check-in
     * message.
     */
    private Long mLastUsedMillis = null;

    /** The unique and anonymous user identifier to include with the message. */
    private String mId;

    /**
     * Configures this builder to create a check-in message using the specified value for the
     * Unix time in milliseconds of the last usage of the <code>kiji</code> script.
     *
     * @param lastUsedMillis is the Unix time, in milliseconds, of the last usage of the
     *     <code>kiji</code> script included with BentoBox.
     * @return this instance, so configuration can be chained.
     */
    public Builder withLastUsedMillis(Long lastUsedMillis) {
      mLastUsedMillis = lastUsedMillis;
      return this;
    }

    /**
     * Configures this builder to create a check-in message using the specified unique and
     * anonymous identifier for the user.
     *
     * @param id that is unique and anonymous for the client sending the built message.
     * @return this instance, so configuration can be chained.
     */
    public Builder withId(String id) {
      mId = id;
      return this;
    }

    /**
     * Gets the value for the system property with the specified key. If querying for the system
     * property returns <code>null</code>, this method will throw an
     * {@link IllegalArgumentException}. This method may also throw the same runtime exceptions
     * thrown by {@link System#getProperty(String)}.
     *
     * @param key to look-up in system properties.
     * @return the value of the system property.
     */
    private String getSystemProperty(String key) {
      String value = System.getProperty(key);
      if (null == value) {
        throw new IllegalArgumentException("There was no value for system property: " + key);
      }
      return value;
    }

    /**
     * Uses system properties to get the operating system name, version,
     * and architecture formatted into a single string.
     *
     * @return a string containing operating system name, version, and architecture.
     */
    private String getOperatingSystem() {
      String osName = getSystemProperty("os.name");
      String osVersion = getSystemProperty("os.version");
      String osArchitecture = getSystemProperty("os.arch");
      return String.format("%s %s %s", osName, osVersion, osArchitecture);
    }

    /**
     * Creates a new check-in message using values configured through this builder and the values
     * of system properties.
     *
     * @return a new check-in message with content configured through this builder and system
     *     properties.
     * @throws IllegalArgumentException if a value returned for a system property was
     *     <code>null</code>, or if a <code>null</code> key was somehow used to query for a system
     *     property.
     * @throws IOException if there is a problem retrieving the current version of BentoBox in use.
     * @throws NullPointerException if values required to create a check-in message were not
     *     configured using the builder.
     * @throws SecurityException if a security manager exists and disallows access to a needed
     *     system property.
     */
    public UpgradeCheckin build()
        throws IllegalArgumentException, IOException, NullPointerException, SecurityException {
      // Ensure the client has completely configured the builder.
      checkNotNull(mId, "User id not supplied to check-in message builder.");
      checkNotNull(mLastUsedMillis, "Last usage timestamp for kiji script not supplied to "
          + "check-in message builder.");
      // Get other message parameters using system properties.
      String operatingSystem = getOperatingSystem();
      String bentoVersion = VersionInfo.getSoftwareVersion();
      String javaVersion = getSystemProperty("java.version");
      return new UpgradeCheckin(MESSAGE_TYPE, MESSAGE_FORMAT, operatingSystem, bentoVersion,
          javaVersion, mLastUsedMillis, mId);
    }
  }
}
