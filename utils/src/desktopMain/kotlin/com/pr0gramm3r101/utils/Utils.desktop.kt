package com.pr0gramm3r101.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.andreypfau.kotlinx.crypto.AES
import io.github.andreypfau.kotlinx.crypto.Pbkdf2
import io.github.andreypfau.kotlinx.crypto.SHA256Digest
import java.util.Base64
import kotlin.random.Random

actual fun Modifier.clearFocusOnKeyboardDismiss() = this

@Composable
actual fun ToggleNavScrimEffect(enabled: Boolean) {}

actual val materialYouAvailable get() = false

class SHA256Digest : Digest {
    override val digestSize: Int = 32
    override val blockSize: Int = 64
    override val algorithmName: String = "SHA-256"

    private val state = UIntArray(8)
    private val buffer = ByteArray(64)
    private var bufferPos = 0
    private var byteCount = 0L

    init {
        reset()
    }

    override fun update(byte: Byte): Digest {
        buffer[bufferPos++] = byte
        if (bufferPos == buffer.size) {
            processBlock()
        }
        byteCount++
        return this
    }

    override fun update(source: ByteArray, startIndex: Int, endIndex: Int): Digest {
        var pos = startIndex
        while (pos < endIndex) {
            buffer[bufferPos++] = source[pos++]
            if (bufferPos == buffer.size) {
                processBlock()
            }
            byteCount++
        }
        return this
    }

    override fun digest(): ByteArray {
        val result = ByteArray(digestSize)
        digest(result)
        return result
    }

    override fun digest(destination: ByteArray, destinationOffset: Int) {
        // Add padding
        val bitLength = byteCount * 8
        update(0x80.toByte())
        while (bufferPos != 56) {
            update(0.toByte())
        }
        // Add length
        for (i in 0..7) {
            update((bitLength shr (56 - i * 8)).toByte())
        }

        // Copy result
        for (i in 0..7) {
            val word = state[i]
            destination[destinationOffset + i * 4] = (word shr 24).toByte()
            destination[destinationOffset + i * 4 + 1] = (word shr 16).toByte()
            destination[destinationOffset + i * 4 + 2] = (word shr 8).toByte()
            destination[destinationOffset + i * 4 + 3] = word.toByte()
        }
    }

    override fun reset() {
        byteCount = 0
        bufferPos = 0
        state[0] = 0x6a09e667u
        state[1] = 0xbb67ae85u
        state[2] = 0x3c6ef372u
        state[3] = 0xa54ff53au
        state[4] = 0x510e527fu
        state[5] = 0x9b05688cu
        state[6] = 0x1f83d9abu
        state[7] = 0x5be0cd19u
    }

    private fun processBlock() {
        val w = UIntArray(64)
        for (i in 0..15) {
            w[i] = (buffer[i * 4].toUInt() shl 24) or
                    (buffer[i * 4 + 1].toUInt() shl 16) or
                    (buffer[i * 4 + 2].toUInt() shl 8) or
                    buffer[i * 4 + 3].toUInt()
        }
        for (i in 16..63) {
            val s0 = (w[i - 15] rotateRight 7) xor (w[i - 15] rotateRight 18) xor (w[i - 15] shr 3)
            val s1 = (w[i - 2] rotateRight 17) xor (w[i - 2] rotateRight 19) xor (w[i - 2] shr 10)
            w[i] = w[i - 16] + s0 + w[i - 7] + s1
        }

        var a = state[0]
        var b = state[1]
        var c = state[2]
        var d = state[3]
        var e = state[4]
        var f = state[5]
        var g = state[6]
        var h = state[7]

        for (i in 0..63) {
            val s1 = (e rotateRight 6) xor (e rotateRight 11) xor (e rotateRight 25)
            val ch = (e and f) xor (e.inv() and g)
            val temp1 = h + s1 + ch + K[i] + w[i]
            val s0 = (a rotateRight 2) xor (a rotateRight 13) xor (a rotateRight 22)
            val maj = (a and b) xor (a and c) xor (b and c)
            val temp2 = s0 + maj

            h = g
            g = f
            f = e
            e = d + temp1
            d = c
            c = b
            b = a
            a = temp1 + temp2
        }

        state[0] += a
        state[1] += b
        state[2] += c
        state[3] += d
        state[4] += e
        state[5] += f
        state[6] += g
        state[7] += h

        bufferPos = 0
    }

    companion object {
        private val K = uintArrayOf(
            0x428a2f98u, 0x71374491u, 0xb5c0fbcfu, 0xe9b5dba5u, 0x3956c25bu, 0x59f111f1u, 0x923f82a4u, 0xab1c5ed5u,
            0xd807aa98u, 0x12835b01u, 0x243185beu, 0x550c7dc3u, 0x72be5d74u, 0x80deb1feu, 0x9bdc06a7u, 0xc19bf174u,
            0xe49b69c1u, 0xefbe4786u, 0x0fc19dc6u, 0x240ca1ccu, 0x2de92c6fu, 0x4a7484aau, 0x5cb0a9dcu, 0x76f988dau,
            0x983e5152u, 0xa831c66du, 0xb00327c8u, 0xbf597fc7u, 0xc6e00bf3u, 0xd5a79147u, 0x06ca6351u, 0x14292967u,
            0x27b70a85u, 0x2e1b2138u, 0x4d2c6dfcu, 0x53380d13u, 0x650a7354u, 0x766a0abbu, 0x81c2c92eu, 0x92722c85u,
            0xa2bfe8a1u, 0xa81a664bu, 0xc24b8b70u, 0xc76c51a3u, 0xd192e819u, 0xd6990624u, 0xf40e3585u, 0x106aa070u,
            0x19a4c116u, 0x1e376c08u, 0x2748774cu, 0x34b0bcb5u, 0x391c0cb3u, 0x4ed8aa4au, 0x5b9cca4fu, 0x682e6ff3u,
            0x748f82eeu, 0x78a5636fu, 0x84c87814u, 0x8cc70208u, 0x90befffau, 0xa4506cebu, 0xbef9a3f7u, 0xc67178f2u
        )
    }
}

actual fun String.encrypt(password: String): String {
    // Generate a random salt
    val salt = ByteArray(16).also { Random.nextBytes(it) }
    
    // Derive key using PBKDF2
    val pbkdf2 = Pbkdf2(
        digest = SHA256Digest(),
        password = password.encodeToByteArray(),
        salt = salt,
        iterationCount = 65536
    )
    val key = pbkdf2.deriveKey(32) // 256 bits
    
    // Generate random IV
    val iv = ByteArray(16).also { Random.nextBytes(it) }
    
    // Create AES cipher
    val cipher = AES(key)
    
    // Pad the input data to be a multiple of block size
    val paddedData = padData(this.encodeToByteArray())
    
    // Encrypt the data in CBC mode
    val encryptedBytes = ByteArray(paddedData.size)
    var previousBlock = iv
    
    for (i in paddedData.indices step 16) {
        val block = ByteArray(16)
        System.arraycopy(paddedData, i, block, 0, 16)
        
        // XOR with previous block (or IV for first block)
        for (j in 0 until 16) {
            block[j] = (block[j] xor previousBlock[j])
        }
        
        // Encrypt the block
        cipher.encryptBlock(block, encryptedBytes, i, 0)
        previousBlock = encryptedBytes.copyOfRange(i, i + 16)
    }
    
    // Combine salt:iv:encrypted data and encode to base64
    return "${salt.encodeBase64()}:${iv.encodeBase64()}:${encryptedBytes.encodeBase64()}"
}

actual fun String.decrypt(password: String): String {
    // Split the input string into its components
    val parts = this.split(":")
    val salt = parts[0].decodeBase64()
    val iv = parts[1].decodeBase64()
    val encryptedBytes = parts[2].decodeBase64()
    
    // Derive key using PBKDF2
    val pbkdf2 = Pbkdf2(
        digest = SHA256Digest(),
        password = password.encodeToByteArray(),
        salt = salt,
        iterationCount = 65536
    )
    val key = pbkdf2.deriveKey(32) // 256 bits
    
    // Create AES cipher
    val cipher = AES(key)
    
    // Decrypt the data in CBC mode
    val decryptedBytes = ByteArray(encryptedBytes.size)
    var previousBlock = iv
    
    for (i in encryptedBytes.indices step 16) {
        val block = ByteArray(16)
        System.arraycopy(encryptedBytes, i, block, 0, 16)
        
        // Decrypt the block
        cipher.decryptBlock(block, decryptedBytes, i, 0)
        
        // XOR with previous block (or IV for first block)
        for (j in 0 until 16) {
            decryptedBytes[i + j] = (decryptedBytes[i + j] xor previousBlock[j])
        }
        
        previousBlock = encryptedBytes.copyOfRange(i, i + 16)
    }
    
    // Remove padding
    val unpaddedData = unpadData(decryptedBytes)
    return String(unpaddedData)
}

// Helper functions for base64 encoding/decoding
private fun ByteArray.encodeBase64(): String {
    return Base64.getEncoder().encodeToString(this)
}

private fun String.decodeBase64(): ByteArray {
    return Base64.getDecoder().decode(this)
}

// PKCS7 padding implementation
private fun padData(data: ByteArray): ByteArray {
    val blockSize = 16
    val paddingLength = blockSize - (data.size % blockSize)
    val paddedData = ByteArray(data.size + paddingLength)
    System.arraycopy(data, 0, paddedData, 0, data.size)
    for (i in data.size until paddedData.size) {
        paddedData[i] = paddingLength.toByte()
    }
    return paddedData
}

private fun unpadData(data: ByteArray): ByteArray {
    val paddingLength = data[data.size - 1].toInt() and 0xFF
    return data.copyOfRange(0, data.size - paddingLength)
}