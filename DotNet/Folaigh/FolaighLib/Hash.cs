/* 
    Copyright 2005 Karmashave.org/Terry Lacy

    This file is part of Folaigh.

    Folaigh is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 2.1 of the License, or
    (at your option) any later version.

    Folaigh is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Folaigh; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
using System;
using org.bouncycastle.crypto;
using org.bouncycastle.crypto.digests;

namespace org.karmashave.folaigh
{
	public class Hash
	{
		private Hash()
		{
		}
		public static byte[] getHash(byte[] input)
		{
			Digest  digest = new SHA256Digest();
			byte[] output = new byte[digest.getDigestSize()];
			digest.update(input, 0, input.Length);
			digest.doFinal(output, 0);
			return output;
		}
	}
}
