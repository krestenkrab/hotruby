/***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2002-2004 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
 * Copyright (C) 2004 Charles O Nutter <headius@headius.com>
 * Copyright (C) 2004 Stefan Matthias Aust <sma@3plus4.de>
 * Copyright (C) 2006 Ola Bini <ola.bini@ki.se>
 * Copyright (C) 2006 Kresten Krab Thorup <krab@trifork.com>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package com.trifork.hotruby.marshal;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.trifork.hotruby.objects.IRubyClass;
import com.trifork.hotruby.objects.IRubyModule;
import com.trifork.hotruby.objects.IRubyObject;
import com.trifork.hotruby.objects.IRubySymbol;
import com.trifork.hotruby.runtime.CallContext;
import com.trifork.hotruby.runtime.RubyRuntime;

/**
 * Unmarshals objects from strings or streams in Ruby's marsal format.
 *
 * @author Anders
 */
public class UnmarshalStream extends FilterInputStream {
    protected final RubyRuntime runtime;
    private UnmarshalCache cache;

    public UnmarshalStream(RubyRuntime runtime, InputStream in) throws IOException {
        super(in);
        this.runtime = runtime;
        this.cache = new UnmarshalCache(runtime);

        in.read(); // Major
        in.read(); // Minor
    }

    public IRubyObject unmarshalObject(CallContext ctx) throws IOException {
        return unmarshalObject(null, ctx);
    }

    public IRubyObject unmarshalObject(IRubyObject proc, CallContext ctx) throws IOException {
        int type = readUnsignedByte();
        IRubyObject result;
        if (cache.isLinkType(type)) {
            result = cache.readLink(this, type);
        } else {
        	result = unmarshalObjectDirectly(type, proc, ctx);
        }
        return result;
    }

    public void registerLinkTarget(IRubyObject newObject) {
        cache.register(newObject);
    }

    private IRubyObject unmarshalObjectDirectly(int type, IRubyObject proc, CallContext ctx) throws IOException {
    	IRubyObject rubyObj = null;
        switch (type) {
        	case 'I':
                rubyObj = unmarshalObject(proc, ctx);
                defaultInstanceVarsUnmarshal(rubyObj, proc, ctx);
        		break;
            case '0' :
                rubyObj = runtime.getNil();
                break;
            case 'T' :
                rubyObj = runtime.getTrue();
                break;
            case 'F' :
                rubyObj = runtime.getFalse();
                break;
            case '"' :
                rubyObj = runtime.unmarshalStringFrom(this);
                break;
            case 'i' :
                rubyObj = runtime.unmarshalFixnumFrom(this);
                break;
            case 'f' :
            	rubyObj = runtime.unmarshalFloatFrom(this);
            	break;
            case ':' :
                rubyObj = runtime.unmarshalSymbolFrom(this);
                break;
            case '[' :
                rubyObj = runtime.unmarshalArrayFrom(this, ctx);
                break;
            case '{' :
                rubyObj = runtime.unmarshalHashFrom(this, ctx);
                break;
            case 'c' :
                rubyObj = runtime.unmarshalClassFrom(this);
                break;
            case 'm' :
                rubyObj = runtime.unmarshalModuleFrom(this);
                break;
            case 'l' :
                rubyObj = runtime.unmarshalBignumFrom(this);
                break;
            case 'S' :
                rubyObj = runtime.unmarshalStructFrom(this);
                break;
            case 'o' :
                rubyObj = defaultObjectUnmarshal(proc, ctx);
                break;
            case 'u' :
                rubyObj = userUnmarshal(ctx);
                break;
            case 'U' :
                rubyObj = userNewUnmarshal(ctx);
                break;
            case 'C' :
            	rubyObj = uclassUnmarshall(ctx);
            	break;
            default :
                throw getRuntime().newArgumentError("dump format error(" + (char)type + ")");
        }
        
        if (proc != null) {
			proc.callMethod("call", new IRubyObject[] {rubyObj}, CallContext.NULL);
		}
        
        return rubyObj;
    }


    public RubyRuntime getRuntime() {
        return runtime;
    }

    public int readUnsignedByte() throws IOException {
        int result = read();
        if (result == -1) {
            throw new IOException("Unexpected end of stream");
        }
        return result;
    }

    public byte readSignedByte() throws IOException {
        int b = readUnsignedByte();
        if (b > 127) {
            return (byte) (b - 256);
        }
		return (byte) b;
    }

    public String unmarshalString() throws IOException {
        int length = unmarshalInt();
        byte[] buffer = new byte[length];
        int bytesRead = read(buffer);
        if (bytesRead != length) {
            throw new IOException("Unexpected end of stream");
        }
        return new String(buffer, "ISO8859_1");
    }

    public int unmarshalInt() throws IOException {
        int c = readSignedByte();
        if (c == 0) {
            return 0;
        } else if (4 < c && c < 128) {
            return c - 5;
        } else if (-129 < c && c < -4) {
            return c + 5;
        }
        long result;
        if (c > 0) {
            result = 0;
            for (int i = 0; i < c; i++) {
                result |= (long) readUnsignedByte() << (8 * i);
            }
        } else {
            c = -c;
            result = -1;
            for (int i = 0; i < c; i++) {
                result &= ~((long) 0xff << (8 * i));
                result |= (long) readUnsignedByte() << (8 * i);
            }
        }
        return (int) result;
    }

    private IRubyObject defaultObjectUnmarshal(IRubyObject proc, CallContext ctx) throws IOException {
        IRubySymbol className = (IRubySymbol) unmarshalObject(ctx);

        // ... FIXME: handle if class doesn't exist ...

        IRubyClass type = (IRubyClass) runtime.getClassFromPath(className.asSymbol());

        assert type != null : "type shouldn't be null.";

        IRubyObject result = type.newInstance();
        registerLinkTarget(result);

        defaultInstanceVarsUnmarshal(result, proc, ctx);

        return result;
    }
    
    private void defaultInstanceVarsUnmarshal(IRubyObject object, IRubyObject proc, CallContext ctx) throws IOException {
    	int count = unmarshalInt();
    	
    	for (int i = 0; i < count; i++) {
    		object.setInstanceVariable(unmarshalObject(ctx).asSymbol(), unmarshalObject(proc, ctx));
    	}
    }
    
    private IRubyObject uclassUnmarshall(CallContext ctx) throws IOException {
    	IRubySymbol className = (IRubySymbol)unmarshalObject(ctx);
    	
    	IRubyClass type = (IRubyClass)runtime.getClassFromPath(className.asSymbol());
    	
    	IRubyObject result = unmarshalObject(ctx);
    	
    	if (result.get_class() != type) {
    		throw new IOException("cannot reassign class pointer from "+result.inspect()+" to "+type.inspect());
    	}
    	
    	return result;
    }

    private IRubyObject userUnmarshal(CallContext ctx) throws IOException {
        String className = unmarshalObject(ctx).asSymbol();
        String marshaled = unmarshalString();
        IRubyModule classInstance = runtime.getModule(className);
        IRubyObject result = classInstance.callMethod(
            "_load",
            new IRubyObject[] { runtime.newString(marshaled) },
            ctx);
        registerLinkTarget(result);
        return result;
    }

    private IRubyObject userNewUnmarshal(CallContext ctx) throws IOException {
        String className = unmarshalObject(ctx).asSymbol();
        IRubyObject marshaled = unmarshalObject(ctx);
        IRubyClass classInstance = runtime.getClass(className);
        IRubyObject result = classInstance.newInstance(new IRubyObject[0], ctx);;
        result.callMethod("marshal_load", new IRubyObject[] { marshaled }, ctx);
        registerLinkTarget(result);
        return result;
    }
}
